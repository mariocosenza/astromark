#!/bin/bash

# Script to set up astromark.jar as a systemd service on Ubuntu

# Variables
JAR_PATH="/opt/astromark/astromark.jar"
SERVICE_NAME="astromark"
ENV_FILE="/etc/astromark.env"

# Ensure the script is run as root
if [[ $EUID -ne 0 ]]; then
   echo "Error: This script must be run as root."
   exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check if the JAR file exists
if [[ ! -f "$JAR_PATH" ]]; then
    echo "Error: JAR file not found at $JAR_PATH"
    exit 1
fi

# Function to prompt for environment variables
prompt_env_var() {
    local var_name="$1"
    local var_value
    while true; do
        read -sp "Enter value for $var_name: " var_value
        echo
        if [[ -n "$var_value" ]]; then
            echo "$var_name=$var_value"
            break
        else
            echo "Value for $var_name cannot be empty. Please enter a value."
        fi
    done
}

# Create environment file
echo "Creating environment file at $ENV_FILE"

{
    prompt_env_var "SPRING_DATASOURCE_URL"
    prompt_env_var "SPRING_DATASOURCE_USERNAME"
    prompt_env_var "SPRING_DATASOURCE_PASSWORD"
    prompt_env_var "SPRING_MAIL_KEY"
    prompt_env_var "JWT_SECRET"
    prompt_env_var "KEYSTORE_PASSWORD"
    prompt_env_var "AWS_ACCESS_KEY"
    prompt_env_var "AWS_SECRET_KEY"
    prompt_env_var "AWS_BUCKET_NAME"
    prompt_env_var "AWS_ENDPOINT"
} > $ENV_FILE

# Set permissions for the env file
chmod 600 $ENV_FILE
chown root:root $ENV_FILE

# Create systemd service file
echo "Creating systemd service file for $SERVICE_NAME"

cat <<EOL > /etc/systemd/system/$SERVICE_NAME.service
[Unit]
Description=Astromark Spring Boot Application
After=network.target

[Service]
User=www-data
Group=www-data
EnvironmentFile=$ENV_FILE
ExecStart=/usr/bin/java -jar $JAR_PATH --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=astromark

[Install]
WantedBy=multi-user.target
EOL

# Reload systemd daemon to recognize the new service
echo "Reloading systemd daemon..."
systemctl daemon-reload

# Enable the service to start on boot
echo "Enabling $SERVICE_NAME service to start on boot..."
systemctl enable $SERVICE_NAME

# Start the service
echo "Starting $SERVICE_NAME service..."
systemctl start $SERVICE_NAME

# Check the status of the service
echo "Checking $SERVICE_NAME service status..."
systemctl status $SERVICE_NAME --no-pager

# Set up port forwarding from 80 to 8080 and 443 to 8443
echo "Setting up port forwarding from 80 to 8080 and 443 to 8443"

# Install iptables-persistent if not already installed
if ! dpkg -l | grep -q iptables-persistent; then
    echo "Installing iptables-persistent..."
    DEBIAN_FRONTEND=noninteractive apt-get update
    DEBIAN_FRONTEND=noninteractive apt-get install -y iptables-persistent
fi

# Function to add iptables rule if it doesn't exist
add_iptables_rule() {
    local protocol=$1
    local dport=$2
    local toport=$3
    if ! iptables -t nat -C PREROUTING -p "$protocol" --dport "$dport" -j REDIRECT --to-port "$toport" 2>/dev/null; then
        iptables -t nat -A PREROUTING -p "$protocol" --dport "$dport" -j REDIRECT --to-port "$toport"
        echo "Added iptables rule: Redirect port $dport to $toport"
    else
        echo "Iptables rule for port $dport already exists. Skipping."
    fi
}

# Add port forwarding rules
add_iptables_rule "tcp" "80" "8080"
add_iptables_rule "tcp" "443" "8443"

# Save the iptables rules
echo "Saving iptables rules..."
netfilter-persistent save

echo "Port forwarding setup complete."

echo "Astromark setup complete."

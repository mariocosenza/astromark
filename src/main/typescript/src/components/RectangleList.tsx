import React from "react";
import {Box, Button, Typography} from "@mui/material";
import {useNavigate} from "react-router";

export type Item = {
    id: number;
    title: string;
    description: string;
}

export type Props= {
    items: Item[];
}

export const RectangleList: React.FC<Props> = ({ items }) => {
    const navigate = useNavigate();

    const handleNavigation = (id: number) => {
        navigate(`/tab/${id}`);
    };

    return (
        <Box
            sx={{
                display: "grid",
                gridTemplateColumns: {
                    xs: "1fr",
                    sm: "repeat(2, 1fr)",
                },
                width: "80%",
                gap: 2,
                margin: "4vh 10% 10% 4vh",
            }}
        >
            {items.map((item) => (
                <Button
                    key={item.id}
                    onClick={() => handleNavigation(item.id)}
                    variant="outlined"
                    sx={{
                        backgroundColor: "#ffffff",
                        border: "1px solid #e0e0e0",
                        borderRadius: "12px",
                        minHeight: { xs: "150px", sm: "180px" }, // Adaptive height
                        width: "100%", // Full width within grid
                        textAlign: "left",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "center",
                        alignItems: "flex-start",
                        boxShadow: "0px 2px 4px rgba(0, 0, 0, 0.1)",
                        paddingBottom: { xs: "10%", sm: "20%" },
                        transition: "transform 0.2s, box-shadow 0.2s",
                        '&:hover': {
                            transform: "scale(1.02)",
                            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.2)",
                        },
                    }}
                >
                    <Typography variant="h5" sx={{ fontWeight: "bold", marginBottom: "8px" }}>
                        {item.title}
                    </Typography>
                    <Typography variant="body2" sx={{ color: "#757575" }}>
                        {item.description}
                    </Typography>
                </Button>
            ))}
        </Box>
    );
};

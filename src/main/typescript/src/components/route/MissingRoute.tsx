import {Navigate} from 'react-router';
import React from "react";

export const MissingRoute: React.FC = () => {
    return < Navigate to={{pathname: '/'}}/>
};



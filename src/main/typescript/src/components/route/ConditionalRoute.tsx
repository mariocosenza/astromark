import React from "react";
import {Navigate} from "react-router";

export const ConditionalRoute = ({node, condition,}: { node: React.ReactNode; condition: () => boolean; }) => {
    return condition() ? <>{node}</> : <Navigate to="/" replace/>;
};
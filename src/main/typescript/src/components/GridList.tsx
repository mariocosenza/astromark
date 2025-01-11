import React from "react";
import {Box, Typography} from "@mui/material";

export type Item = {
    id: number;
    title: string;
    description: string;
}

export type Props= {
    items: Item[];
    onClick: (id: number) => void;
}

export const GridList: React.FC<Props> = ({items, onClick}) => {

    return (
        <Box className="grid-container">
            {items.map((item) => (
                <button key={item.id} className="grid-item" onClick={() => onClick(item.id)}>
                    <Typography variant="h5" className="title" fontWeight="bold">
                        {item.title}
                    </Typography>

                    <Typography variant="body2" color={'textSecondary'}>
                        {item.description}
                    </Typography>
                </button>
            ))}
        </Box>
    );
};

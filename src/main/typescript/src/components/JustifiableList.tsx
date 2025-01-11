import { Avatar, Box, IconButton, TextField, Typography } from "@mui/material";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import React from "react";
import axiosConfig from "../services/AxiosConfig.ts";
import { Env } from "../Env.ts";
import { SelectedStudent } from "../services/StateService.ts";

export type JustificationProp = {
    id: string;
    needsJustification: boolean;
    justified: boolean;
    justificationText: string;
    avatar: string;
    hexColor: string;
    date: string;
};

export type JustificationListProp = {
    list: JustificationProp[];
    absence: boolean;
};

function justify(justification: string, id: string, absence: boolean): boolean {
    try {
        if (justification === "") {
            return false;
        }
        axiosConfig.patch(
            `${Env.API_BASE_URL}/students/${SelectedStudent.id}/justifiable/${id}/justify?absence=${absence}`,
            justification.replaceAll('"', ""),
            { headers: { "Content-Type": "text/plain" } }
        );
        return true;
    } catch (error) {
        console.error(error);
    }
    return false;
}

export const JustifiableList: React.FC<JustificationListProp> = (props) => {
    const [data, setData] = React.useState<JustificationProp[]>(props.list);

    const handleTextChange = (index: number, value: string) => {
        setData((prevData) => {
            const newData = [...prevData];
            newData[index] = {
                ...newData[index],
                justificationText: value,
            };
            return newData;
        });
    };

    const handleJustify = (index: number) => {
        const item = data[index];
        if (justify(item.justificationText, item.id, props.absence)) {
            setData((prevData) => {
                const newData = [...prevData];
                newData[index] = {
                    ...newData[index],
                    justified: true,
                };
                return newData;
            });
        }
    };

    return (
        <div>
            {data.map((item, i) => (
                <Box
                    flexWrap="wrap"
                    className="listItem"
                    style={{ minHeight: "5rem" }}
                    width="60vw"
                    sx={{ mb: "0.5rem" }}
                    key={"list" + i}
                >
                    <Avatar sx={{ bgcolor: item.hexColor, ml: "1%", mt: "0.8rem" }}>{item.avatar}</Avatar>
                    <div style={{ marginLeft: "1%", display: "flex", flexDirection: "column" }}>
                        <Typography variant="h6" ml={"1vw"} mt={"0.8rem"}>
                            {  props.absence? 'Assenza del' : 'Ritardo del' } {item.date} {
                                item.justified && '- Giustifica: ' + item.justificationText

                        }
                        </Typography>
                    </div>
                    {!item.justified && (
                        <>
                            <TextField
                                sx={{ mt: "0.5rem" }}
                                style={{ marginLeft: "1%", width: "55%" }}
                                label="Justificazione"
                                value={item.justificationText}
                                onChange={(e) => handleTextChange(i, e.currentTarget.value)}
                            />
                            <IconButton sx={{ mt: "0.2rem", ml: '10%'}} onClick={() => handleJustify(i)} style={{color: 'darkgreen'}}>
                                <EditOutlinedIcon />
                            </IconButton>
                        </>
                    )}
                </Box>
            ))}
        </div>
    );
};

import React from 'react';
import ZemosoIcon from '../../assets/images/zemoso_logo.png';
import { makeStyles, Typography } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
    image: {
        display: "block",
        height: "400px",
        width: "400px",
        marginTop: "5%",
        marginLeft: "auto",
        marginRight: "auto",
    },
    text: {
        fontFamily: "Roboto",
        fontSize: "28px",
        fontWeight: 500,
        fontStretch: "normal",
        fontStyle: "normal",
        lineHeight: 1.56,
        letterSpacing: "normal",
        color: "#3e3f42",
        flexGrow: 1,
        textAlign: "center",
        marginTop: "20px"
    }
}));

export default function AccessDeniedpage() {
    const classes = useStyles();
    return (
        <div>
            <img className={classes.image} src={ZemosoIcon} alt="zemoso_logo" />
            <Typography className={classes.text}>
                This site is restricted for you, try logging in from ZeMoSoLabs organization mail.
            </Typography>
        </div>
    );
}
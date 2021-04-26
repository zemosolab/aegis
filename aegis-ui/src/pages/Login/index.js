import React from "react";
import zemoso from "../../assets/images/zemoso_logo.png";
import { makeStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import GoogleButton from "../../components/molecules/GoogleButton";
const useStyles = makeStyles((theme) => ({
  leftBox: {
    backgroundColor: "#d8d8d8",
    flexGrow: "1",
  },
  rightBox: {
    display: "flex",
    flexDirection: "column",
    flexGrow: "1"
  }
  ,
  container: {
    display: "flex",
    height: "100%",
    position: "absolute",
    left: "0",
    width: "100%",
    overflow: "hidden"
  },
  text: {
    alignSelf: "flex-start",
    marginLeft: theme.spacing(5),
    marginBottom: theme.spacing(3),
    color: "#737373",
    verticalAlign:"middle"
  },
  text2: {
    alignSelf: "flex-start",
    marginLeft: theme.spacing(5),
    marginBottom: theme.spacing(3),
    color: "#737373",
    verticalAlign:"middle"
  },
  Button: {
    alignSelf: "flex-start",
    marginLeft: theme.spacing(5),
    verticalAlign:"middle"
  },
  logo: {
    alignSelf: "flex-end",
    padding: "15px",
    marginBottom: theme.spacing(20),
  },
}));
const Login = () => {
  const classes = useStyles();
  return (
    <div className={classes.container}>
      <div className={classes.leftBox}></div>
      <div className={classes.rightBox}>
        <img
          src={zemoso}
          alt="Zemoso Logo"
          height="110"
          width="110"
          className={classes.logo}
        />
        <div className={classes.text}>    

          <Typography variant="h3">Hi There!</Typography>
        </div>
        <Typography variant="h4" className={classes.text2}>
          We are happy to have you here
        </Typography>
        <div className={classes.Button}>
          <GoogleButton />
        </div>
      </div>

    </div>
  );
};

export default Login;

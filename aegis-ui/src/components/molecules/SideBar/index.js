import React from "react";
import Tabs from "@material-ui/core/Tabs";
import { makeStyles } from "@material-ui/core/styles";
import Tab from "@material-ui/core/Tab";

const useStyles = makeStyles((theme) => ({
  tab: {
    width: "290px",
    elevation: "50deg",
    boxShadow: "1px 0 0 0 #f1f1f3",
    backgroundColor: "#ffffff",
  },
  Tabs: {
    boxShadow: "1px 0 0 0 #f1f1f3",
    marginTop: "8%",
  },
  font: {
    color: "black",
    textTransform: "none",
    fontSize: "14px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.57,
    letterSpacing: "normal",
  },
  ind: {
    backgroundColor: "#1665D8",
  },
  bgColor: {
    backgroundColor: "#f6f9fd",
  },
  wrapper: {
    marginLeft: theme.spacing(2),
    alignItems: "flex-start",
  },
}));

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    "aria-controls": `simple-tabpanel-${index}`,
  };
}

const SideBar = (props) => {
  const classes = useStyles();

  return (
    <div className={classes.tab}>
      <Tabs
        value={props.value}
        onChange={props.handleChange}
        aria-label="simple tabs example"
        orientation="vertical"
        textColor="primary"
        classes={{ indicator: classes.ind }}
        className={classes.Tabs}
      >
        <Tab
          label="MAIN MENU"
          {...a11yProps(0)}
          classes={{ wrapper: classes.wrapper }}
          disabled={true}
        />
        <Tab
          label="Dashboard"
          {...a11yProps(1)}
          className={classes.font}
          classes={{ wrapper: classes.wrapper, selected: classes.bgColor }}
        />
        <Tab
          label="Card Management"
          {...a11yProps(2)}
          className={classes.font}
          classes={{ wrapper: classes.wrapper, selected: classes.bgColor }}
        />
        <Tab
          label="User Management"
          {...a11yProps(3)}
          className={classes.font}
          classes={{ wrapper: classes.wrapper, selected: classes.bgColor }}
        />
        <Tab
          label="Reports"
          {...a11yProps(4)}
          className={classes.font}
          classes={{ wrapper: classes.wrapper, selected: classes.bgColor }}
        />
        <Tab
          label="Settings"
          {...a11yProps(5)}
          className={classes.font}
          classes={{ wrapper: classes.wrapper, selected: classes.bgColor }}
        />
      </Tabs>
      <div className={classes.Tabs}>.</div>
    </div>
  );
};

export default SideBar;

import React, { useState, useEffect } from "react";

import { makeStyles, withStyles } from "@material-ui/core/styles";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import "../../assets/css/zeplin.css";
import { getAllUsers } from "../../services/UserMgmtService";
import AssignUser from "../AssignUser";
import ExtendOrRemoveUser from "../ExtendOrRemoveUser";
import AddGuest from "../../components/organisms/AddGuest";

const useStyles = makeStyles((theme) => ({
  demo1: {
    backgroundColor: theme.palette.background.paper,
  },
  tabText: {
    height: "24px",
    fontFamily: "Roboto",
    fontSize: "16px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.5,
    letterSpacing: "normal",
    color: "#3e3f42",
  },
  tabPanel: {
    paddingLeft: "20px",
    paddingTop: "10px",
  },
  cards: {
    marginTop: "4%",
    justifyContent: "space-around",
  },
  cardContainer: {
    marginTop: "4%",
  },
}));

const UserManagement = () => {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);
  const [loading, setLoading] = useState(true);

  const TabComponents = [
    <AssignUser loading={loading} setLoading={setLoading} />,
    <div>
      <AddGuest />
    </div>,
    <ExtendOrRemoveUser loading={loading} setLoading={setLoading} />,
  ];
  return (
    <>
      <div className={classes.cardContainer}>
        <div className={classes.cards}>
          <div>
            <CustomizedTabs value={value} setValue={setValue} />
            {TabComponents[value]}
          </div>
        </div>
      </div>
    </>
  );
};

export default UserManagement;

const AntTabs = withStyles({
  root: {
    borderBottom: "0px solid #1665d8",
  },
  indicator: {
    backgroundColor: "#1890ff",
  },
})(Tabs);

const AntTab = withStyles((theme) => ({
  root: {
    textTransform: "none",
    minWidth: 72,
    fontWeight: theme.typography.fontWeightRegular,
    marginRight: theme.spacing(4),
    "&$selected": {
      color: "#3e3f42",
      fontWeight: theme.typography.fontWeightMedium,
    },
    "&:focus": {
      color: "#3e3f42",
    },
  },
  selected: {},
}))((props) => <Tab disableRipple {...props} />);

function CustomizedTabs(props) {
  const classes = useStyles();
  const handleChange = (event, newValue) => {
    props.setValue(newValue);
  };

  return (
    <div className={classes.root}>
      <div className={classes.demo1}>
        <AntTabs
          className={classes.tabPanel}
          value={props.value}
          onChange={handleChange}
          aria-label="ant example"
        >
          <AntTab className={classes.tabText} label="Assign Card" />
          <AntTab className={classes.tabText} label="Add Guest" />
          <AntTab className={classes.tabText} label="Remove/Extend" />
        </AntTabs>
      </div>
    </div>
  );
}

import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import "../../assets/css/zeplin.css";
import AppBar from "../../components/molecules/AegisAppBar";
import SideBar from "../../components/molecules/SideBar";
import DashBoard from "../DashBoard";
import { useSelector, useDispatch } from "react-redux";
import CardManagement from "../CardManagement";
import UserManagement from "../UserManagement";

const useStyles = makeStyles((theme) => ({
  orientation: {
    display: "flex",
    height: "935px",
    marginTop: "3%",
  },
}));

const Skeleton = (props) => {
  const [value, setValue] = React.useState(1);
  const dispatch = useDispatch();
  const profile = useSelector((state) => state.AuthReducer.profile);
  const classes = useStyles();
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const mapper = new Map([
    [1, <DashBoard value={value} setValue={setValue} />],
    [
      2,
      <div>
        <CardManagement value={value} setValue={setValue} />
      </div>,
    ],
    [
      3,
      <div>
        <UserManagement />
      </div>,
    ],
  ]);

  return (
    <div>
      <AppBar src={profile.picture} username={profile.given_name} />
      <div className={classes.orientation}>
        <SideBar value={value} handleChange={handleChange} />
        <div className="container">
          <div className="base">{mapper.get(value)}</div>
        </div>
      </div>
    </div>
  );
};

export default Skeleton;

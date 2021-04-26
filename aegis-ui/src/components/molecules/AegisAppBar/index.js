import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import NotificationsOutlinedIcon from "@material-ui/icons/NotificationsOutlined";
import zemoso from "../../../assets/images/zemoso_logo.png";
import ImageAvatars from "../../atoms/AvatarProfile";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import { IconButton } from "@material-ui/core";
import { logout } from "../../../services/AuthService";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
    color: "black",
    marginLeft: "1%",
  },
  icon: {
    color: "black",
    marginLeft: "2%",
  },
  name: {
    color: "black",
    marginRight: "2%",
  },
}));

const ButtonAppBar = (props) => {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <div className={classes.root}>
      <AppBar
        elevation={0}
        style={{
          background: "#FFFFFF",
          boxShadow: "0 1px 0 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        <Toolbar>
          <img src={zemoso} alt="Zemoso Logo" height="30" width="30" />
          <Typography variant="h6" className={classes.title}>
            Aegis
          </Typography>
          <NotificationsOutlinedIcon className={classes.icon} />
          <ImageAvatars src={props.src} />
          <div className={classes.name}>{props.username}</div>
          <IconButton onClick={handleClick}>
            <ExpandMoreIcon style={{ color: "black" }} />
          </IconButton>
          <Menu
            id="simple-menu"
            anchorEl={anchorEl}
            keepMounted
            open={Boolean(anchorEl)}
            onClose={handleClose}
          >
            <MenuItem onClick={logout}>Logout</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>
    </div>
  );
};

export default ButtonAppBar;

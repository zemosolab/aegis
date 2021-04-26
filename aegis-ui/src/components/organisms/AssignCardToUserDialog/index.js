import React, { useState } from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import { FormControl, NativeSelect, Divider } from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Button from "@material-ui/core/Button";
import InputBase from "@material-ui/core/InputBase";
import { completeAction } from "../../../services/CardMgmtService";
import { useEffect } from "react";
import ActionNotifier from "../../atoms/ActionNotifier";
import { getUsersByType } from "../../../services/UserMgmtService";

const useStyles = makeStyles((theme) => ({
  dialogWidth: {
    width: "570px",
    minHeight: "300px",
  },
  empDetails: {
    display: "flex",
    flexDirection: "row",
    fontFamily: "Roboto",
    fontSize: "12px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.5,
    letterSpacing: "normal",
    color: "#9ea0a5",
  },
  headingText: {
    fontFamily: "Roboto",
    fontSize: "20px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.5,
    letterSpacing: "normal",
    color: "#3e3f42",
  },
  selectCardText: {
    fontFamily: "Roboto",
    fontSize: "16px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.5,
    letterSpacing: "normal",
    color: "#3e3f42",
  },
  empDetailsMargin: {
    marginRight: "165px",
  },
  dialogDropdown: {
    display: "flex",
    flexDirection: "row",
    marginBottom: "40px",
  },
  divider: {
    marginBottom: "20px",
  },
  saveButton: {
    textTransform: "none",
    marginRight: "15px",
    marginBottom: "20px",
  },
  cancelButton: {
    textTransform: "none",
    marginRight: "10px",
    marginBottom: "20px",
  },
  margin: {
    width: "280px",
    height: "40px",
    marginTop: "10px",
  },
  dropDownIcon: {
    marginRight: "10px",
  },
}));

const BootstrapInput = withStyles((theme) => ({
  input: {
    borderRadius: 4,
    position: "relative",
    border: "1px solid #ced4da",
    fontSize: 16,
    padding: "10px 26px 10px 12px",
    transition: theme.transitions.create(["border-color", "box-shadow"]),
    "&:focus": {
      borderRadius: 4,
      borderColor: "#80bdff",
      boxShadow: "0 0 0 0.2rem rgba(0,123,255,.25)",
    },
  },
}))(InputBase);

export default function AssignCardToUserDialog(props) {
  const classes = useStyles();
  const cardNo = props.cardNo;
  const cardName = props.cardname;
  const [userId, setUserId] = useState();
  const [users, setUsers] = useState([]);
  const [color, setColor] = useState("#e0e0e0");
  const [textColor, setTextColor] = useState("#000");

  const handleCard = (event) => {
    setUserId(event.target.value);
    setColor("#2d9c3c");
    setTextColor("#fff");
  };

  useEffect(() => {
    async function getUsersFromDB() {
      let response = await getUsersByType("UNASSIGNED");
      setUsers(response);
    }
    getUsersFromDB();
  }, []);

  const assignCardToUser = async () => {
    const response = await completeAction(
      "ASSIGN",
      "Assign Card to Employee",
      userId,
      cardNo
    );
    props.handleClose();
    props.setMessage("ASSIGNED CARD");
    response.status === 200 ? props.setOpen(true) : props.setOpenError(true);
    props.setRender(!props.render);
  };

  return (
    <div>
      <Dialog
        classes={{ paperWidthSm: classes.dialogWidth }}
        open={props.open}
        onClose={props.handleClose}
        aria-labelledby="form-dialog-title"
      >
        <DialogTitle id="form-dialog-title" className={classes.headingText}>
          Assign Card
        </DialogTitle>
        <Divider light variant="fullWidth" className={classes.divider} />
        <DialogContent id="form-dialog-title">
          <div className={classes.empDetails}>
            <div className={classes.empDetailsMargin}>CARD ID:</div>
            {cardName}
          </div>
        </DialogContent>
        <div className={classes.dialogDropdown}>
          <DialogContent
            id="form-dialog-title"
            className={classes.selectCardText}
          >
            SELECT THE USER:
          </DialogContent>
          <DialogContent>
            <FormControl className={classes.margin}>
              <NativeSelect
                classes={{ icon: classes.dropDownIcon }}
                id="demo-customized-select-native"
                value={userId}
                onChange={handleCard}
                input={<BootstrapInput />}
              >
                <option value="">Select Any</option>
                {users.map((user) =>
                  user.userType === "GUEST" ? (
                    <option value={user.id}>{user.username}</option>
                  ) : (
                    <option value={user.id}>{user.userEmail}</option>
                  )
                )}
              </NativeSelect>
            </FormControl>
          </DialogContent>
        </div>
        <Divider light variant="fullWidth" className={classes.divider} />
        <DialogActions>
          <Button
            disableElevation
            onClick={props.handleClose}
            variant="contained"
            className={classes.cancelButton}
          >
            Cancel
          </Button>
          <Button
            disableElevation
            style={{ background: color, color: textColor }}
            onClick={assignCardToUser}
            variant="contained"
            className={classes.saveButton}
          >
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

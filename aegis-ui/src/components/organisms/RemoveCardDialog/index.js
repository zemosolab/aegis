import React, { useState } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import { Button, FormControlLabel, Snackbar } from "@material-ui/core";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControl from "@material-ui/core/FormControl";
import { completeAction } from "../../../services/CardMgmtService";
import ActionNotifier from "../../atoms/ActionNotifier";
import MuiAlert from "@material-ui/lab/Alert";

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const useStyles = makeStyles((theme) => ({
  dialogWidth: {
    width: "560px",
  },
  title: {
    padding: "30px",
  },
  saveButton: {
    width: "75px",
    height: "38px",
    borderRadius: "4px",
    boxShadow:
      "0 1px 1px 0 rgba(19, 31, 21, 0.1), inset 0 2px 0 0 rgba(255, 255, 255, 0.06)",
    border: "solid 1px #2d9c3c",
    backgroundImage: "linear-gradient(to top, #34aa44, #38b249)",
    color: "#fff",
  },
}));

export default function RemoveCardDialog(props) {
  const classes = useStyles();
  const [value, setValue] = React.useState("");

  const handleChange = (event) => {
    setValue(event.target.value);
  };

  const removeCardToUser = async () => {
    const response = await completeAction(
      value.toUpperCase(),
      `${value.toUpperCase()} Card to Employee`,
      props.userId,
      props.cardId
    );
    props.handleClose();
    props.setMessage("CARD REMOVED");
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
        <DialogTitle id="form-dialog-title" className={classes.title}>
          Remove card to {props.name}!
        </DialogTitle>
        <DialogContent>
          Why do you want to do so?
          <br />
          <br />
          <FormControl>
            <RadioGroup
              aria-label="gender"
              name="Reason"
              value={value}
              onChange={handleChange}
            >
              <FormControlLabel
                value="UNASSIGN"
                control={<Radio />}
                label="Remove"
              />
              <FormControlLabel
                value="BLOCK"
                control={<Radio />}
                label="Block and Remove"
              />
            </RadioGroup>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button
            disableElevation
            onClick={props.handleClose}
            variant="contained"
          >
            No
          </Button>
          <Button
            disableElevation
            className={classes.saveButton}
            onClick={removeCardToUser}
            variant="contained"
          >
            Yes
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

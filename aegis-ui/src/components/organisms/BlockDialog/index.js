import React from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import { makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import { Divider } from "@material-ui/core";
const useStyles = makeStyles(theme => ({
  dialogWidth: {
    width: "560px"
  },
  tableColumnHeading: {
    width: "170px",
    height: "18px",
    fontFamily: "Roboto",
    fontSize: "12px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.56,
    letterSpacing: "normal",
    color: "#9ea0a5"
  }
}));
function BlockDialog(props) {
  const classes = useStyles();
  return (
    <div>
      <Dialog
        classes={{ paperWidthSm: classes.dialogWidth }}
        open={props.open}
        onClose={props.handleClose}
        aria-labelledby="form-dialog-title"
      >
        <DialogTitle id="form-dialog-title">{props.empName}</DialogTitle>
        <Divider variant="fullWidth" />
        <DialogTitle id="form-dialog-title">
          Card ID: {props.cardId}
        </DialogTitle>
        <DialogContent>
          <DialogContentText>Reason for blocking</DialogContentText>
          <TextField
            autoFocus
            style={{ width: "515px" }}
            margin="dense"
            variant="outlined"
            multiline
            rows="5"
          />
        </DialogContent>
        <DialogActions>
          <Button
            disableElevation
            onClick={props.handleClose}
            variant="contained"
          >
            Cancel
          </Button>
          <Button
            disableElevation
            onClick={props.handleClose}
            variant="contained"
          >
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default BlockDialog;

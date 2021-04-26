import React, { useState } from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Divider, DialogContentText, TextField } from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Button from "@material-ui/core/Button";
import { completeAction } from "../../../services/CardMgmtService";
import ActionNotifier from "../../atoms/ActionNotifier";

const useStyles = makeStyles((theme) => ({
  dialogWidth: {
    width: "570px",
    minHeight: "300px",
  },
  cardDetails: {
    display: "flex",
    flexDirection: "row",
    fontFamily: "Roboto",
    fontSize: "18px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.5,
    letterSpacing: "normal",
    color: "#9ea0a5",
    marginBottom: "15px",
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
    fontSize: "12px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.5,
    letterSpacing: "normal",
    color: "#3e3f42",
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
}));

export default function BlockCardDialog(props) {
  const classes = useStyles();

  const blockCard = async () => {
    const response = await completeAction(
      "BLOCK",
      "Block Card",
      null,
      props.cardNo
    );
    console.log(response.status === 200);
    props.handleClose();
    props.setMessage("CARD BLOCKED");
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
          Block Card
        </DialogTitle>
        <Divider light variant="fullWidth" className={classes.divider} />
        <DialogContent id="form-dialog-title">
          <div className={classes.cardDetails}>
            <div>Card ID: </div>
            {props.cardName}
          </div>
          <DialogContentText className={classes.selectCardText}>
            REASON FOR BLOCKING
          </DialogContentText>
          <TextField
            autoFocus
            style={{ width: "515px" }}
            margin="dense"
            variant="outlined"
            multiline
            rows="5"
          />
        </DialogContent>
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
            onClick={blockCard}
            variant="contained"
            className={classes.saveButton}
          >
            Block
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

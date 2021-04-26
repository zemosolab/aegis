import React from "react";
import { Snackbar } from "@material-ui/core";
import MuiAlert from "@material-ui/lab/Alert";

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function ActionNotifier(props) {
  return (
    <>
      <Snackbar
        open={props.open}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
        autoHideDuration={3000}
        onClose={props.handleClose}
      >
        <Alert onClose={props.handleClose} severity="success">
          {props.message} successfully!
        </Alert>
      </Snackbar>
      <Snackbar
        open={props.openError}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
        autoHideDuration={3000}
        onClose={props.handleCloseError}
      >
        <Alert severity="error" onClose={props.handleCloseError}>
          {props.message} failed!
        </Alert>
      </Snackbar>
    </>
  );
}

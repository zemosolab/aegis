import React, { useState } from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import { Divider, FormControl, NativeSelect } from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Button from "@material-ui/core/Button";
import InputBase from "@material-ui/core/InputBase";
import { completeAction, getAllCards } from "../../../services/CardMgmtService";
import { useEffect } from "react";
import ActionNotifier from "../../atoms/ActionNotifier";

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
    marginRight: "140px",
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

export default function AssignCardDialog(props) {
  const classes = useStyles();
  const [cardNo, setCardNo] = useState();
  const [cards, setCards] = useState([]);
  const [color, setColor] = useState("#e0e0e0");
  const [textColor, setTextColor] = useState("#000");
  const [open, setOpen] = React.useState(false);
  const [openError, setOpenError] = useState(false);
  const [message, setMessage] = useState("");
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  const handleCloseError = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpenError(false);
  };

  const handleCard = (event) => {
    setCardNo(event.target.value);
    setColor("#2d9c3c");
    setTextColor("#fff");
  };

  useEffect(() => {
    async function getCardsFromDB() {
      let response = await getAllCards();
      setCards(response);
    }
    getCardsFromDB();
  }, []);

  const assignCardToUser = async () => {
    const response = await completeAction(
      "ASSIGN",
      "Assign Card to Employee",
      props.userId,
      cardNo
    );
    response.status === 200 ? setOpen(true) : setOpenError(true);
    props.handleClose();
    props.setFname(!props.fname);
    setMessage("ASSIGNED CARD");
  };

  return (
    <div>
      <ActionNotifier
        message={message}
        open={open}
        setOpen={setOpen}
        openError={openError}
        setOpenError={setOpenError}
        handleClose={handleClose}
        handleCloseError={handleCloseError}
      />
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
            <div className={classes.empDetailsMargin}>EMP NAME/ID:</div>
            {props.empName} / {props.cardId}
          </div>
        </DialogContent>
        <div className={classes.dialogDropdown}>
          <DialogContent
            id="form-dialog-title"
            className={classes.selectCardText}
          >
            SELECT THE CARD:
          </DialogContent>
          <DialogContent>
            <FormControl className={classes.margin}>
              <NativeSelect
                classes={{ icon: classes.dropDownIcon }}
                id="demo-customized-select-native"
                value={cardNo}
                onChange={handleCard}
                input={<BootstrapInput />}
              >
                <option value="">Select Any</option>
                {cards.map((card) =>
                  card.cardStatus === "UNASSIGNED" ||
                  card.cardStatus === "UNBLOCKED" ? (
                    <option value={card.id}>{card.hardwareId}</option>
                  ) : null
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

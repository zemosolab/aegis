import React, { useState, useEffect } from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import { TextField, FormControl, NativeSelect } from "@material-ui/core";
import {
  KeyboardDatePicker,
  MuiPickersUtilsProvider,
} from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import InputBase from "@material-ui/core/InputBase";
import { postUserToDB } from "../../../services/UserMgmtService";
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/material.css";
import { getAllCards } from "../../../services/CardMgmtService";
import ActionNotifier from "../../atoms/ActionNotifier";

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    flexDirection: "column",
  },
  margin: {
    width: "300px",
    height: "40px",
  },
  text: {
    alignSelf: "center",
    marginRight: "150px",
  },
  phone: {
    alignSelf: "center",
    marginRight: "140px",
  },
  selectCard: {
    alignSelf: "center",
    marginRight: "90px",
  },
  date: {
    alignSelf: "center",
    marginRight: "113px",
  },
  names: {
    display: "flex",
    flexDirection: "row",
    marginLeft: "30px",
    marginTop: "40px",
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
    marginLeft: "225px",
    marginTop: "40px",
  },
  calenderWidth: {
    width: "180px",
    marginRight: "30px",
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

export default function AddGuest() {
  const classes = useStyles();
  const [type, setType] = useState("GUEST");
  const [startDate, handleStartDateChange] = useState(new Date());
  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [cards, setCards] = useState([]);
  const [endDate, handleEndDateChange] = useState(new Date());
  const [cardNo, setCardNo] = useState();
  const [open, setOpen] = React.useState(false);
  const [failedImport, setFailedImport] = useState(false);

  const handleFailedImport = () => {
    setFailedImport(true);
  };

  const handleCloseFailedImport = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setFailedImport(false);
  };
  const handleClick = () => {
    setOpen(true);
  };
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  const addUser = async () => {
    postUserToDB(
      cardNo,
      fullName,
      startDate,
      endDate,
      type,
      phone,
      handleClick,
      handleFailedImport
    );
    setName("");
    setLastName("");
    setPhone("");
    setCardNo(null);
  };

  const handleName = (event) => {
    setName(event.target.value);
  };
  const handleLastName = (event) => {
    setLastName(event.target.value);
    setFullName(name + " " + event.target.value);
  };
  const handlePhone = (event) => {
    setPhone(event);
  };
  const handleCard = (event) => {
    setCardNo(event.target.value);
  };

  useEffect(() => {
    async function getCardsFromDB() {
      let response = await getAllCards();
      setCards(response);
    }
    getCardsFromDB();
  }, []);

  return (
    <div className={classes.root}>
      <ActionNotifier
        open={open}
        handleClose={handleClose}
        message={type + " added"}
        openError={failedImport}
        handleCloseError={handleCloseFailedImport}
      />
      <div className={classes.names}>
        <div className={classes.text}>NAME:</div>
        <TextField
          required
          style={{ marginRight: "10px" }}
          placeholder="FirstName"
          margin="normal"
          variant="outlined"
          value={name}
          onChange={handleName}
        />
        <TextField
          required
          placeholder="LastName"
          margin="normal"
          variant="outlined"
          value={lastName}
          onChange={handleLastName}
        />
      </div>
      <div className={classes.names}>
        <div className={classes.phone}>PHONE:</div>
        <PhoneInput
          required
          country={"in"}
          value={phone}
          onChange={handlePhone}
        />
      </div>
      <div className={classes.names}>
        <div className={classes.date}>DURATION:</div>
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <KeyboardDatePicker
            required
            disableToolbar
            disablePast
            inputVariant="outlined"
            variant="inline"
            format="yyyy/MM/dd"
            margin="normal"
            id="date-picker-inline"
            label="STARTS"
            value={startDate}
            className={classes.calenderWidth}
            onChange={handleStartDateChange}
            KeyboardButtonProps={{
              "aria-label": "change date",
            }}
          />
          <KeyboardDatePicker
            minDate={startDate}
            required
            disableToolbar
            disablePast
            inputVariant="outlined"
            variant="inline"
            format="yyyy/MM/dd"
            margin="normal"
            id="date-picker-inline"
            label="ENDS"
            value={endDate}
            className={classes.calenderWidth}
            onChange={handleEndDateChange}
            KeyboardButtonProps={{
              "aria-label": "change date",
            }}
          />
        </MuiPickersUtilsProvider>
      </div>
      <div className={classes.names}>
        <div className={classes.selectCard}>ASSIGN CARD:</div>
        <FormControl className={classes.margin}>
          <NativeSelect
            classes={{ icon: classes.dropDownIcon }}
            required
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
      </div>
      <button className={classes.saveButton} onClick={addUser}>
        Save
      </button>
    </div>
  );
}

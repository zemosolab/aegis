import React, { useState, useEffect } from 'react';
import Backdrop from '@material-ui/core/Backdrop';
import CircularProgress from '@material-ui/core/CircularProgress';
import { makeStyles } from "@material-ui/core/styles";
import EnhancedTable from "../../components/molecules/EnhancedTable";
import "../../assets/css/zeplin.css";
import ControlPointIcon from "@material-ui/icons/ControlPoint";
import { Toolbar, InputAdornment, IconButton } from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import {
  getCompleteActionOfCard,
  getCardTableInfo,
} from "../../services/CardMgmtService";
import AddCardToList from "../AddCardToList";
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from "@material-ui/lab/Alert";
import SearchIcon from "@material-ui/icons/Search";

const useStyles = makeStyles((theme) => ({
  cards: {
    marginTop: "4%",
    justifyContent: "space-around",
  },
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: '#fff',
  },
  cardContainer: {
    marginTop: "4%",
  },
  tableHeading: {
    width: "100%",
    backgroundColor: "rgba(255, 255, 255, 0)",
    boxShadow: "0 1px 0 0 #eaedf3",
    display: "flex",
    flexDirection: "row",
  },
  headingText: {
    width: "22px",
    height: "28px",
    fontFamily: "Roboto",
    fontSize: "18px",
    fontWeight: 500,
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: 1.56,
    letterSpacing: "normal",
    color: "#3e3f42",
    flexGrow: 1,
    textAlign: "left",
    paddingLeft: "4%",
    paddingBottom: "10px",
  },
  button: {
    width: "138px",
    height: "45px",
    borderRadius: "4px",
    boxShadow:
      "0 1px 1px 0 rgba(22, 29, 37, 0.1), inset 0 2px 0 0 rgba(255, 255, 255, 0.06)",
    border: "solid 1px #1461d2",
    backgroundImage: "linear-gradient(to top, #1665d8, #1f6fe5)",
    color: "#fff",
    textAlign: "center",
    alignItems: "center",
    justifyContent: "center",
    marginRight: "30px",
    marginBottom: "28px",
    display: "flex",
    flexDirection: "row",
    fontSize: "14px",
  },
  searchBox: {
    border: "solid 0px #e2e5ed",
    background: "#fff",
    color: "#3e3f42",
    marginRight: "20px",
    marginTop: "0px",
    width: "250px",
  },
}));

function createData(
  idOfCard,
  cardId,
  dateOfCardIssued,
  status,
  empName_Id,
  action,
  userId
) {
  return {
    idOfCard,
    cardId,
    dateOfCardIssued,
    status,
    empName_Id,
    action,
    userId,
  };
}

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}
const CardManagement = (props) => {
  const classes = useStyles();
  const [addCard, setAddCard] = React.useState(false);
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newRows, setNewRows] = useState([]);
  let mapper = new Map();
  const [open, setOpen] = React.useState(false);
  const [render, setRender] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");

  const handleSearchTerm = (event) => {
    setSearchTerm(event.target.value);
  };

  const renderCards = async () => {
    const cards = await getCardTableInfo();
    let records = [];
    let cardActions = [];
    for (let i in cards) {
      let data = cards[i];
      cardActions = await getCompleteActionOfCard(data.id);
      cardActions.map((card) => {
        mapper.set(data.id, card.createdAt);
      });
      records.push(
        createData(
          data.id,
          data.hardwareId,
          mapper.get(data.id).substring(0, 10),
          data.cardStatus,
          data.name,
          "A",
          data.userId
        )
      );
    }
    setRows(records);
    setNewRows(records);
    setLoading(false);
  };
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  useEffect(() => {
    renderCards();

  }, [render]);

  useEffect(() => {
    const filterTable = () => {
      const filteredRows = rows.filter((row) =>
        row.cardId.toLowerCase().includes(searchTerm)
      );
      setNewRows(filteredRows);
    };
    filterTable();
  }, [searchTerm]);

  return (
    <div>
      <Backdrop className={classes.backdrop} open={loading}>
        <CircularProgress color="inherit" />
      </Backdrop>
      <div className={classes.cardContainer}>
        <div className={classes.cards}>
          <Toolbar />
          <Snackbar
            open={open}
            anchorOrigin={{ vertical: "top", horizontal: "center" }}
            autoHideDuration={6000}
            onClose={handleClose}
          >
            <Alert onClose={handleClose} severity="success">
              card is successfully added
            </Alert>
          </Snackbar>
          {addCard ? (
            <AddCardToList
              setRender={setRender}
              render={render}
              addCard={setAddCard}
              setOpen={setOpen}
            />
          ) : (
              <div>
                <div>
                  <div className={classes.tableHeading}>
                    <div className={classes.headingText}>All</div>
                    <TextField
                      margin="normal"
                      variant="outlined"
                      onChange={handleSearchTerm}
                      className={classes.searchBox}
                      InputProps={{
                        style: { height: "45px", paddingRight: "0px" },
                        endAdornment: (
                          <InputAdornment>
                            <IconButton>
                              <SearchIcon />
                            </IconButton>
                          </InputAdornment>
                        ),
                      }}
                    />
                    <button
                      className={classes.button}
                      onClick={() => setAddCard(true)}
                    >
                      <ControlPointIcon style={{ marginRight: "8px" }} />
                    Add New
                  </button>
                  </div>
                  <EnhancedTable
                    type="card"
                    rows={newRows}
                    headerType="card"
                    render={render}
                    setRender={setRender}
                  />
                </div>
              </div>
            )}
        </div>
      </div>
    </div>
  );
};

export default CardManagement;
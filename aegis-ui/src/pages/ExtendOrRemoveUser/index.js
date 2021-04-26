import React, { useEffect, useState } from "react";
import CircularProgress from '@material-ui/core/CircularProgress';
import EnhancedTable from "../../components/molecules/EnhancedTable";
import { makeStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import { getUsersByType } from "../../services/UserMgmtService";
import { InputAdornment, IconButton, Backdrop } from "@material-ui/core";
import SearchIcon from "@material-ui/icons/Search";

const useStyles = makeStyles((theme) => ({
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: '#fff',
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
    marginTop: "25px",
    textAlign: "left",
    paddingLeft: "4%",
    paddingBottom: "10px",
  },
  searchBox: {
    border: "solid 0px #e2e5ed",
    background: "#fff",
    color: "#3e3f42",
    marginRight: "20px",
    width: "250px",
  },
}));

const ExtendOrRemoveUser = (props) => {
  const classes = useStyles();
  let record = [];
  const [rows, setRows] = React.useState([]);
  const [newRows, setNewRows] = useState([]);
  const [render, setRender] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const handleSearchTerm = (event) => {
    setSearchTerm(event.target.value);
  };

  useEffect(() => {
    const getCardHolders = async () => {
      props.setLoading(true);
      const users = await getUsersByType("ASSIGNED");
      if (!users.message) {
        const cardHolders = users.filter((user) => user.hardwareId !== null);
        cardHolders.map((user) => {
          record.push({
            idOfCard: user.idOfCard,
            userId: user.id,
            empName: user.userType !== "GUEST" ? user.userEmail : user.username,
            empId: user.employeeId !== null ? user.employeeId : "NA",
            cardId: user.cardname,
            dateOfJoining: user.createdAt,
            typeOfEmployee: user.userType,
            action: "A",
          });
        });
      }
      setRows(record);
      setNewRows(record);
     props.setLoading(false);
    };
    getCardHolders();
  }, [render]);

  useEffect(() => {
    const filterTable = () => {
      const filteredRows = rows.filter((row) =>
        row.empName.toLowerCase().includes(searchTerm)
      );
      setNewRows(filteredRows);
    };
    filterTable();
  }, [searchTerm]);

  return (
    <div>
            <Backdrop className={classes.backdrop} open={props.loading}>
          <CircularProgress color="inherit" />
        </Backdrop>
      <div className={classes.tableHeading}>
        <div className={classes.headingText}></div>
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
      </div>
      <EnhancedTable
        render={render}
        setRender={setRender}
        rows={newRows}
        type="extend"
      />
    </div>
  );
};

export default ExtendOrRemoveUser;

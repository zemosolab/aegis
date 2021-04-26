import React, { useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import EnhancedTable from "../../components/molecules/EnhancedTable";
import "../../assets/css/zeplin.css";
import ControlPointIcon from "@material-ui/icons/ControlPoint";
import TextField from "@material-ui/core/TextField";
import ReactFileReader from "react-file-reader";
import { createUsers, getUsersByType } from "../../services/UserMgmtService";
import SearchIcon from "@material-ui/icons/Search";
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from "@material-ui/lab/Alert";
import { csvToObject } from "../../utils/helper";
import IconButton from "@material-ui/core/IconButton";
import { InputAdornment, Icon, Backdrop, CircularProgress } from "@material-ui/core";

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

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
    marginTop: "15px",
    display: "flex",
    flexDirection: "row",
    fontSize: "14px",
  },
  searchBox: {
    border: "solid 0px #e2e5ed",
    background: "#fff",
    color: "#3e3f42",
    marginRight: "20px",
    width: "250px",
    verticalAlign: "middle",
  },
}));

const AssignUser = (props) => {
  const classes = useStyles();
  const [fname, setFname] = useState(true);
  const [rows, setRows] = useState([]);
  const [open, setOpen] = React.useState(false);
  const [failedImport, setFailedImport] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const [newRows, setNewRows] = useState([]);

  const handleSearchTerm = (event) => {
    setSearchTerm(event.target.value);
  };

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

  const renderUsers = async () => {
    props.setLoading(true);
    let data = await getUsersByType("UNASSIGNED");
    let records = [];
    data.map((row) => {
      records.push({
        empId: row.employeeId !== null ? row.employeeId : "NA",
        empName: row.userType !== "GUEST" ? row.userEmail : row.username,
        dateOfJoining: row.dateOfJoining,
        action: "A",
        userId: row.id,
      });
    });
    if (records.length !== rows.length) {
      setRows(records);
      setNewRows(records);
    }
    props.setLoading(false);
  };

  useEffect(() => {
    renderUsers();
  }, [fname]);

  useEffect(() => {
    const filterTable = () => {
      const filteredRows = rows.filter((row) =>
        row.empName.toLowerCase().includes(searchTerm)
      );
      setNewRows(filteredRows);
    };
    filterTable();
  }, [searchTerm]);

  const handleFiles = (files) => {
    const reader = new FileReader();
    reader.onload = function (e) {
      let obj = csvToObject(e.target.result, setFailedImport);
      if (obj !== null)
        createUsers(obj, setFname, fname, handleClick, handleFailedImport);
    };
    reader.readAsText(files[0]);
  };
  return (
    <div>
      <>
        <Backdrop className={classes.backdrop} open={props.loading}>
          <CircularProgress color="inherit" />
        </Backdrop>
        <div className={classes.tableHeading}>
          <div className={classes.headingText}></div>
          <Snackbar
            open={open}
            anchorOrigin={{ vertical: "top", horizontal: "center" }}
            autoHideDuration={3000}
            onClose={handleClose}
          >
            <Alert onClose={handleClose} severity="success">
              The users imported successfully!
          </Alert>
          </Snackbar>
          <Snackbar
            open={failedImport}
            anchorOrigin={{ vertical: "top", horizontal: "center" }}
            autoHideDuration={3000}
            onClose={handleCloseFailedImport}
          >
            <Alert severity="error" onClose={handleCloseFailedImport}>
              Some thing went wrong please check your csv file!
          </Alert>
          </Snackbar>
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
          <ReactFileReader handleFiles={handleFiles} fileTypes={".csv"}>
            <button className={classes.button}>
              <ControlPointIcon style={{ marginRight: "5px" }} />
            Upload csv
          </button>
          </ReactFileReader>
        </div>
      </>
      <EnhancedTable
        loading={props.loading}
        setLoading={props.setLoading}
        type="assign"
        rows={newRows}
        fname={fname}
        setFname={setFname}
        headerType="userAssign"
      />
    </div>
  );
};

export default AssignUser;

import React from "react";
import PropTypes from "prop-types";
import TableCell from "@material-ui/core/TableCell";
import { makeStyles } from "@material-ui/core/styles";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";

const useStyles = makeStyles((theme) => ({
  visuallyHidden: {
    border: 0,
    clip: "rect(0 0 0 0)",
    height: 1,
    margin: -1,
    overflow: "hidden",
    padding: 0,
    position: "absolute",
    top: 20,
    width: 1,
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
    color: "#9ea0a5",
  },
}));

export default function EnhancedTableHeadUsers(props) {
  const { order, orderBy, onRequestSort } = props;
  const style = useStyles();
  const createSortHandler = (property) => (event) => {
    onRequestSort(event, property);
  };
  let headCells = [];
  if (props.headerType === "card") {
    headCells = [
      { id: "cardId", numeric: false, disablePadding: false, label: "CARD ID" },
      {
        id: "dateOfCardIssued",
        numeric: false,
        disablePadding: false,
        label: "DATE OF CARD ISSUED",
      },
      { id: "status", numeric: false, disablePadding: false, label: "STATUS" },
      {
        id: "empName_Id",
        numeric: false,
        disablePadding: false,
        label: "EMP NAME/ ID",
      },
      { id: "action", numeric: false, disablePadding: false, label: "ACTION" },
    ];
  } else if (props.headerType === "userAssign") {
    headCells = [
      { id: "empId", numeric: false, disablePadding: false, label: "EMP ID" },
      {
        id: "empName",
        numeric: false,
        disablePadding: false,
        label: "EMP EMAIL",
      },
      {
        id: "dateOfJoining",
        numeric: false,
        disablePadding: false,
        label: "DATE OF JOINING",
      },
      { id: "action", numeric: false, disablePadding: false, label: "ACTION" },
    ];
  } else {
    headCells = [
      {
        id: "EmpName",
        numeric: false,
        disablePadding: false,
        label: "EMP NAME",
      },
      { id: "empId", numeric: false, disablePadding: false, label: "EMP ID" },
      { id: "cardId", numeric: false, disablePadding: false, label: "CARD ID" },
      {
        id: "dateOfCardIssued",
        numeric: false,
        disablePadding: false,
        label: "DATE OF CARD ISSUED",
      },
      {
        id: "TypeOfEmployee",
        numeric: false,
        disablePadding: false,
        label: "TYPE OF EMPLOYEE",
      },
      { id: "action", numeric: false, disablePadding: false, label: "ACTION" },
    ];
  }

  return (
    <TableHead>
      <TableRow>
        <TableCell></TableCell>
        {headCells.map((headCell) => (
          <TableCell
            key={headCell.id}
            align={headCell.numeric ? "right" : "justify"}
            padding={headCell.disablePadding ? "none" : "default"}
            sortDirection={orderBy === headCell.id ? order : false}
          >
            <TableSortLabel
              IconComponent={ArrowDropDownIcon}
              active={orderBy === headCell.id}
              direction={orderBy === headCell.id ? order : "asc"}
              onClick={createSortHandler(headCell.id)}
              className={style.tableColumnHeading}
            >
              {headCell.label}
              {orderBy === headCell.id ? (
                <span className={style.visuallyHidden}>
                  {order === "desc" ? "sorted descending" : "sorted ascending"}
                </span>
              ) : null}
            </TableSortLabel>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}

EnhancedTableHeadUsers.propTypes = {
  classes: PropTypes.object.isRequired,
  onRequestSort: PropTypes.func.isRequired,
  order: PropTypes.oneOf(["asc", "desc"]).isRequired,
  orderBy: PropTypes.string.isRequired,
  rowCount: PropTypes.number.isRequired,
};

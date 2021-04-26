import React from "react";
import { Tooltip, Fab } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import { completeAction } from "../../../services/CardMgmtService";
import ActionNotifier from "../../atoms/ActionNotifier";
import { useState } from "react";
import AssignCardToUserDialog from "../../organisms/AssignCardToUserDialog";
import PersonAddIcon from "@material-ui/icons/PersonAdd";
import BlockIcon from "@material-ui/icons/Block";
import RemoveCircleOutlineIcon from "@material-ui/icons/RemoveCircleOutline";
import BlockCardDialog from "../../organisms/BlockCardDialog";
import RemoveCardDialog from "../../organisms/RemoveCardDialog";

const useStyles = makeStyles((theme) => ({
  actionButton: {
    background: "#9ea0a5",
    color: "#fff",
    width: "40px",
    height: "30px",
  },
  disableBoxShadow: {
    boxShadow: "none",
    marginRight: "5px",
  },
  tooltip: {
    backgroundColor: theme.palette.common.black,
  },
}));

export default function CardActionButton(props) {
  const classes = useStyles();
  const [cardNo, setCardNo] = useState();
  const [cardName, setCardName] = useState("");
  const [open, setOpen] = React.useState(false);
  const [openError, setOpenError] = useState(false);
  const [openDialog, setOpenDialog] = useState(false);
  const [openBlock, setOpenBlock] = useState(false);
  const [openRemove, setOpenRemove] = useState(false);
  const [userId, setUserId] = useState();
  const [empName, setEmpName] = useState("");
  const [message, setMessage] = useState("");
  const handleCloseDialog = () => {
    setOpenDialog(false);
    props.setRender(!props.render);
  };
  const handleCloseBlockDialog = () => {
    setOpenBlock(false);
    props.setRender(!props.render);
  };
  const handleCloseRemoveDialog = () => {
    setOpenRemove(false);
    props.setRender(!props.render);
  };
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
  const generateLabel = (status, labelType) => {
    let label;
    let tooltipLabel;
    switch (status) {
      case "ASSIGNED":
        label = <RemoveCircleOutlineIcon fontSize="default" />;
        tooltipLabel = "UnAssign Card";
        break;
      case "UNASSIGNED":
        label = <PersonAddIcon fontSize="default" />;
        tooltipLabel = "Assign Card";
        break;
      case "BLOCKED":
        label = "UB";
        tooltipLabel = "Unblock Card";
        break;
      case "UNBLOCKED":
        label = <BlockIcon fontSize="default" />;
        tooltipLabel = "Block Card";
        break;
      default:
        label = <PersonAddIcon fontSize="default" />;
        tooltipLabel = "Assign Card";
        break;
    }
    if (labelType === "Tooltip") return tooltipLabel;
    else return label;
  };

  const performAction = async (status, actionDetails, cardId) => {
    let response;
    switch (status) {
      case "ASSIGNED":
        response = await completeAction(
          "UNASSIGN",
          "Unassign card",
          actionDetails,
          cardId
        );
        response.status === 200 ? setOpen(true) : setOpenError(true);
        props.setRender(!props.render);
        break;
      case "BLOCKED":
        response = await completeAction(
          "UNBLOCK",
          "Unblock card",
          null,
          cardId
        );
        response.status === 200 ? setOpen(true) : setOpenError(true);
        props.setRender(!props.render);
        break;
      default:
        break;
    }
  };

  const mapper = new Map([
    [
      "ASSIGNED",
      <>
        <Tooltip
          title={generateLabel("ASSIGNED", "Tooltip")}
          classes={{ tooltip: classes.tooltip }}
          placement="left"
          arrow
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={() => {
              setOpenRemove(true);
              setCardNo(props.row.idOfCard);
              setEmpName(props.row.empName_Id);
              setUserId(props.row.userId);
              // performAction("ASSIGNED", props.row.userId, props.row.idOfCard);
              setMessage("UNASSIGNED");
            }}
            className={classes.actionButton}
          >
            {generateLabel("ASSIGNED", "Button")}
          </Fab>
        </Tooltip>
        <Tooltip
          title={generateLabel("UNBLOCKED", "Tooltip")}
          classes={{ tooltip: classes.tooltip }}
          placement="right"
          arrow
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={() => {
              setOpenBlock(true);
              setCardNo(props.row.idOfCard);
              setCardName(props.row.cardId);
              setMessage("BLOCKED");
            }}
            className={classes.actionButton}
          >
            {generateLabel("UNBLOCKED", "Button")}
          </Fab>
        </Tooltip>
      </>,
    ],
    [
      "UNASSIGNED",
      <>
        <Tooltip
          title={generateLabel("UNASSIGNED", "Tooltip")}
          classes={{ tooltip: classes.tooltip }}
          placement="left"
          arrow
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={() => {
              setOpenDialog(true);
              setCardNo(props.row.idOfCard);
              setMessage("ASSIGNED");
            }}
            className={classes.actionButton}
          >
            {generateLabel("UNASSIGNED", "Button")}
          </Fab>
        </Tooltip>
        <Tooltip
          title={generateLabel("UNBLOCKED", "Tooltip")}
          classes={{ tooltip: classes.tooltip }}
          placement="right"
          arrow
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={() => {
              setOpenBlock(true);
              setCardNo(props.row.idOfCard);
              setCardName(props.row.cardId);
              setMessage("BLOCKED");
            }}
            className={classes.actionButton}
          >
            {generateLabel("UNBLOCKED", "Button")}
          </Fab>
        </Tooltip>
      </>,
    ],
    [
      "BLOCKED",
      <Tooltip
        title={generateLabel("BLOCKED", "Tooltip")}
        classes={{ tooltip: classes.tooltip }}
        placement="left"
        arrow
      >
        <Fab
          classes={{ root: classes.disableBoxShadow }}
          onClick={() => {
            performAction("BLOCKED", null, props.row.idOfCard);
            setMessage("UNBLOCKED");
          }}
          className={classes.actionButton}
        >
          {generateLabel("BLOCKED", "Button")}
        </Fab>
      </Tooltip>,
    ],
    [
      "UNBLOCKED",
      <>
        <Tooltip
          title={generateLabel("UNASSIGNED", "Tooltip")}
          classes={{ tooltip: classes.tooltip }}
          placement="left"
          arrow
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={() => {
              setOpenDialog(true);
              setCardNo(props.row.idOfCard);
              setMessage("ASSIGNED");
            }}
            className={classes.actionButton}
          >
            {generateLabel("UNASSIGNED", "Button")}
          </Fab>
        </Tooltip>
        <Tooltip
          title={generateLabel("UNBLOCKED", "Tooltip")}
          classes={{ tooltip: classes.tooltip }}
          placement="right"
          arrow
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={() => {
              setOpenBlock(true);
              setCardNo(props.row.idOfCard);
              setCardName(props.row.cardId);
              setMessage("BLOCKED");
            }}
            className={classes.actionButton}
          >
            {generateLabel("UNBLOCKED", "Button")}
          </Fab>
        </Tooltip>
      </>,
    ],
  ]);

  return (
    <>
      {openDialog ? (
        <AssignCardToUserDialog
          cardNo={cardNo}
          cardname={props.row.cardId}
          open={openDialog}
          render={props.render}
          message={message}
          setMessage={setMessage}
          setRender={props.setRender}
          handleClose={handleCloseDialog}
          openSnack={open}
          setOpen={setOpen}
          openError={openError}
          setOpenError={setOpenError}
          handleCloseSnack={handleClose}
          handleCloseError={handleCloseError}
        />
      ) : openBlock ? (
        <BlockCardDialog
          cardNo={cardNo}
          cardName={cardName}
          open={openBlock}
          message={message}
          setMessage={setMessage}
          render={props.render}
          setRender={props.setRender}
          handleClose={handleCloseBlockDialog}
          openSnack={open}
          setOpen={setOpen}
          openError={openError}
          setOpenError={setOpenError}
          handleCloseSnack={handleClose}
          handleCloseError={handleCloseError}
        />
      ) : openRemove ? (
        <RemoveCardDialog
          name={empName}
          userId={userId}
          cardId={cardNo}
          render={props.render}
          setRender={props.setRender}
          message={message}
          setMessage={setMessage}
          open={openRemove}
          openSnack={open}
          setOpen={setOpen}
          openError={openError}
          setOpenError={setOpenError}
          handleCloseSnack={handleClose}
          handleCloseError={handleCloseError}
          handleClose={handleCloseRemoveDialog}
        />
      ) : (
              <>
                <ActionNotifier
                  message={message}
                  open={open}
                  setOpen={setOpen}
                  openError={openError}
                  setOpenError={setOpenError}
                  handleClose={handleClose}
                  handleCloseError={handleCloseError}
                />
                <div key={mapper.get(props.status)}>{mapper.get(props.status)}</div>
              </>
            )}
    </>
  );
}

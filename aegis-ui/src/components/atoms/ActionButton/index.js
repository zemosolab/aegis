import React, { useState } from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Fab } from "@material-ui/core";
import ExtendValidityDialog from "../../organisms/ExtendValidityDialog";
import RemoveCardDialog from "../../organisms/RemoveCardDialog";
import Tooltip from "@material-ui/core/Tooltip";
import CardActionButton from "../../molecules/CardActionButton";
import ScheduleIcon from "@material-ui/icons/Schedule";
import RemoveCircleOutlineIcon from "@material-ui/icons/RemoveCircleOutline";
import PersonAddIcon from "@material-ui/icons/PersonAdd";

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

const ActionButton = (props) => {
  const classes = useStyles();
  const [openRemove, setOpenRemove] = useState(false);
  const [openExtend, setOpenExtend] = useState(false);
  const [openCard, setOpenCard] = useState(false);
  const handleRemoveOpen = () => {
    setOpenRemove(true);
  };
  const handleRemoveClose = () => {
    setOpenRemove(false);
  };
  const handleClick = () => {
    setOpenExtend(true);
  };
  const handleClose = () => {
    setOpenExtend(false);
  };
  const handleCardOpen = () => {
    setOpenCard(true);
  };
  const handleCardClose = () => {
    setOpenCard(false);
  };

  const mapper = new Map([
    [
      "card",
      <CardActionButton
        row={props.row}
        status={props.row.status}
        render={props.render}
        setRender={props.setRender}
        handleCardOpen={handleCardOpen}
        handleCardClose={handleCardClose}
      />,
    ],
    [
      "assign",

      <Tooltip
        title="Assign Card"
        classes={{ tooltip: classes.tooltip }}
        placement="right"
        arrow
      >
        <Fab
          classes={{ root: classes.disableBoxShadow }}
          onClick={() => props.handleClickOpen(props.row, props.idx)}
          className={classes.actionButton}
        >
          <PersonAddIcon fontSize="default" />
        </Fab>
      </Tooltip>,
    ],
    [
      "extend",
      <div>
        <Tooltip
          title="Remove Card"
          placement="left"
          arrow
          classes={{ tooltip: classes.tooltip }}
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={handleRemoveOpen}
            className={classes.actionButton}
          >
            <RemoveCircleOutlineIcon fontSize="default" />
          </Fab>
        </Tooltip>
        <Tooltip
          title="Extend Card Validity"
          placement="right"
          arrow
          classes={{ tooltip: classes.tooltip }}
        >
          <Fab
            classes={{ root: classes.disableBoxShadow }}
            onClick={handleClick}
            className={classes.actionButton}
          >
            <ScheduleIcon fontSize="default" />
          </Fab>
        </Tooltip>
      </div>,
    ],
  ]);
  return (
    <>
      {openExtend ? (
        <ExtendValidityDialog
          message={props.message}
          setMessage={props.setMessage}
          openSuccess={props.open}
          setOpen={props.setOpen}
          openError={props.openError}
          setOpenError={props.setOpenError}
          open={openExtend}
          cardId={props.row.idOfCard}
          handleClose={handleClose}
          userId={props.row.userId}
        />
      ) : (
        [
          openRemove ? (
            <RemoveCardDialog
              message={props.message}
              setMessage={props.setMessage}
              openSuccess={props.open}
              setOpen={props.setOpen}
              openError={props.openError}
              setOpenError={props.setOpenError}
              name={props.row.empName}
              cardId={props.row.idOfCard}
              render={props.render}
              setRender={props.setRender}
              open={openRemove}
              handleClose={handleRemoveClose}
            />
          ) : (
            <div key={mapper.get(props.type)}>{mapper.get(props.type)}</div>
          ),
        ]
      )}
    </>
  );
};

export default ActionButton;

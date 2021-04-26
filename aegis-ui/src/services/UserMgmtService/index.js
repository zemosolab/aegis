import { urls } from "../../utils/constants";
import {
  getAllCards,
  getCompleteAction,
  completeAction,
} from "../CardMgmtService";

export const getAllUsers = async () => {
  const users = await fetch(urls.allUserDetailsUrl, {
    method: "GET",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
  })
    .then((response) => response.json())
    .catch((err) => {
      return [];
    });
  return users;
};

export const postUserToDB = async (
  cardNo,
  empName,
  startDate,
  endDate,
  type,
  phone,
  handleClick,
  handleFailedImport
) => {
  const user = await fetch(urls.addUsersToDB, {
    method: "POST",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
    body: JSON.stringify({
      usersList:[{username: empName,
      hardwareId: cardNo,
      userType: type,
      phoneNo: phone,
      dateOfJoining: startDate,
    }]}),
  })
    .then((resp) => resp.json())
    .then((data) => {
      data.status > 300 ? handleFailedImport() : handleClick();
      return data;
    })
   .then((data) => {
      if (data.length === 1) { completeAction("ASSIGN", "Assign Card", data[0].id, cardNo) }
    });
  return user;
};

export const createUsers = (
  users,
  setFname,
  fname,
  handleClick,
  handleFailedImport
) => {
  fetch(urls.addUsersToDB, {
    method: "POST",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
    body: JSON.stringify({
      usersList: users,
    }),
  })
    .then((data) => data.json())
    .then((data) => {
      data.status > 300 ? handleFailedImport() : handleClick();
      return data;
    })
    .then((data) => setFname(!fname));
};

export const getUser = (userId) => {
  let user = fetch(urls.allUserDetailsUrl + `/${userId}`, {
    method: "get",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
  }).then((data) => data.json());
  return user;
};

export const getUsersByType = async (userType) => {
  let assignedUsers = [];
  let unassignUsers = [];
  let cards = await getAllCards();
  let assignedUserIds = new Set();
  let mapper = new Map();
  for (let i in cards) {
    let card = cards[i];
    if (card.cardStatus === "ASSIGNED") {
      let action = await getCompleteAction(card.id, card.assignmentId);
      if (`userId` in action.details) {
        assignedUserIds.add(action.details.userId);
        mapper.set(action.details.userId, {
          cardname: card.hardwareId,
          cardId: card.id,
          createdAt: action.createdAt.substring(0, 10),
        });
      }
    }
  }

  let users = await getAllUsers();
  users.map((user) => {
    if (assignedUserIds.has(user.id)) {
      assignedUsers.push({
        ...user,
        createdAt: mapper.get(user.id).createdAt,
        cardname: mapper.get(user.id).cardname,
        idOfCard: mapper.get(user.id).cardId,
      });
    } else unassignUsers.push(user);
  });
  if (userType === "ASSIGNED") {
    return assignedUsers;
  } else return unassignUsers;
};

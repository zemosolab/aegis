import { urls } from "../../utils/constants";
import { getUser } from "../UserMgmtService";

export const getAllCards = async () => {
  const cards = await fetch(urls.allCardDetailsUrl, {
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
  return cards;
};

export const postCardToDB = async (cardNo, empName) => {
  const card = await fetch(urls.addCardToDB, {
    method: "POST",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
    body: JSON.stringify({
      hardwareId: cardNo,
      status: "assigned",
    }),
  }).then((resp) => resp.json());
  return card;
};

export const getCard = (cardId) => {
  fetch(urls.allCardDetailsUrl + `/${cardId}`, {
    method: "get",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
  }).then((data) => data.json());
};

export const getCompleteAction = async (cardId, actionId) => {
  const cards = await fetch(
    urls.addCardToDB + `/${cardId}` + "/actions" + `/${actionId}`,
    {
      method: "GET",
      headers: {
        "content-type": "application/json",
        accept: "application/json",
      },
    }
  ).then((data) => data.json());
  return cards;
};

export const getCompleteActionOfCard = async (cardId) => {
  const cards = await fetch(urls.addCardToDB + `/${cardId}` + "/actions", {
    method: "GET",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
  }).then((data) => data.json());
  return cards;
};

export const completeAction = async (
  actionType,
  reason,
  actionDetails,
  cardId,
  additionalDetails
) => {
  const response = await fetch(urls.addCardToDB + `/${cardId}` + "/actions", {
    method: "POST",
    headers: {
      "content-type": "application/json",
      accept: "application/json",
    },
    body: JSON.stringify({
      actionType: actionType,
      reason: reason,
      details: { userId: actionDetails, ...additionalDetails },
    }),
  });
  return response;
};

export const getCardTableInfo = async () => {
  let cards = await getAllCards();
  let allCards = [];
  for (let i in cards) {
    let card = cards[i];
    if (card.cardStatus === "ASSIGNED") {
      let temp = await getCompleteAction(card.id, card.assignmentId);
      let user = await getUser(temp.details.userId);
      let name = user.userEmail;
      if (user.userType === "GUEST") name = user.username;
      allCards.push({ ...card, name: name, userId: user.id });
    } else {
      allCards.push({ ...card, name: "NA", userId: null });
    }
  }
  return allCards;
};

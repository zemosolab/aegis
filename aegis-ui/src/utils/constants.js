export const REDIRECT_URI = process.env.REACT_APP_REDIRECT_URI;
export const GRANT_TYPE = "authorization_code";
export const API_AUDIENCE = process.env.REACT_APP_API_AUDIENCE;
export const urls = {
  tokenAccessUrl: process.env.REACT_APP_TOKEN_ACCESS_URL,
  authorizeUrl: process.env.REACT_APP_AUTHORIZE_URL,

  allCardDetailsUrl: process.env.REACT_APP_CARD_MANAGEMENT_URL,
  addCardToDB: process.env.REACT_APP_CARD_MANAGEMENT_URL,

  allUserDetailsUrl: process.env.REACT_APP_USER_MANAGEMENT_URL,
  addUsersToDB: process.env.REACT_APP_USER_MANAGEMENT_URL,
};

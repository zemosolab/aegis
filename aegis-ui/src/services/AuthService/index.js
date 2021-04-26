import {
  GRANT_TYPE,
  REDIRECT_URI,
  urls,
  API_AUDIENCE,
} from "../../utils/constants.js";
import request from "request";
var jwtDecode = require("jwt-decode");

let isAuthenticated = false;
export const getAccessToken = (authCode) => {
  const options = {
    method: "POST",
    url: urls.tokenAccessUrl,
    headers: { "content-type": "application/x-www-form-urlencoded" },
    form: {
      grant_type: GRANT_TYPE,
      client_id: process.env.REACT_APP_CLIENT_ID,
      client_secret: process.env.REACT_APP_CLIENT_SECRET,
      code: authCode,
      redirect_uri: REDIRECT_URI,
    },
  };
  return new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) reject(error);
      else resolve(JSON.parse(body));
    });
  });
};

export const googleAuthenticate = () => {
  const url = `${urls.authorizeUrl}?response_type=code&client_id=${process.env.REACT_APP_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&scope=openid%20profile%20email%20user_metadata%20picture&state=STAThE&connection=google-oauth2&audience=${API_AUDIENCE}`;
  window.location = url;
};

export const getProfileInfo = async (setProfile) => {
  let queryString = window.location.search;
  if (queryString.includes("code")) {
    const urlParams = new URLSearchParams(queryString);
    let authCode = urlParams.get("code");
    const userInfo = await getAccessToken(authCode);
    const token = jwtDecode(userInfo.id_token);
    isAuthenticated = true;
    setProfile(token);
  }
};

export const logout = () => {
  localStorage.removeItem("profile");
  const url = `${process.env.REACT_APP_LOGOUT}?client_id=${process.env.REACT_APP_CLIENT_ID}&returnTo=${process.env.REACT_APP_URL}`;
  window.location = url;
};

export const isValidUser = () => isAuthenticated;

import { LOGIN_INFO } from "../../actions/actionTypes";
const initialState = {
  isLogged: false,
  profile: JSON.parse(localStorage.getItem("profile")),
};

export default (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_INFO: {
      localStorage.setItem("profile", JSON.stringify(action.payload.profile));
      return {
        ...state,
        profile: action.payload.profile
          ? action.payload.profile
          : state.profile,
        isLogged: action.payload.isLogged,
      };
    }
  }
  return initialState;
};

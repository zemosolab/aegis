import React, { useEffect } from "react";
import { getProfileInfo } from "../../services/AuthService";
import { useSelector, useDispatch } from "react-redux";
import { Redirect } from "react-router-dom";
import LoadingScreen from "react-loading-screen";
import zemoso from "../../assets/images/zemoso_logo.png";
import { LOGIN_INFO } from "../../actions/actionTypes";
import AccessDeniedpage from "../AccessDeniedPage";
const Loading = () => {
  const [profile, setProfile] = React.useState({});
  const [isAsscessDenied,setAccessDenied]=React.useState(false);
  const dispatch = useDispatch();
  let isLog = useSelector((state) => state.AuthReducer);
  useEffect(() => {

    if(window.location.href.includes("error")){
      setAccessDenied(true);
    }
    else{
    async function info() {
      await getProfileInfo(setProfile);
    }
    info();
  }
  }, []);

  useEffect(() => {

    
    if ("name" in profile) {
      dispatch({
        type: LOGIN_INFO,
        payload: { isLogged: true, profile: profile },
      });
    }
  }, [profile]);

  const loginSuccess= (<div>
      <LoadingScreen
        loading={true}
        bgColor="#ffffff"
        spinnerColor="#9ee5f8"
        textColor="#676767"
        logoSrc={zemoso}
        text="Loading"
      ></LoadingScreen>
      {isLog.isLogged ? <Redirect to="/dashboard" /> : "Loading"}
    </div>)

  return (
    <div>

     {isAsscessDenied?<AccessDeniedpage/>:loginSuccess}
    </div>
  );
};

export default Loading;

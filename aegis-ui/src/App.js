import React from "react";
import Login from "./pages/Login";
import { Switch, Route } from "react-router-dom";
import { Provider } from "react-redux";
import Skeleton from "./pages/Skeleton";
import store from "./store";
import Loading from "./pages/Loading";
function App() {
  return (
    <div>
      <Provider store={store}>
        <Switch>
          <Route exact path="/load" component={Loading} />
          <Route exact path="/dashboard">
            <Skeleton />
          </Route>
          <Route exact path="/" component={Login} />
        </Switch>
      </Provider>
    </div>
  );
}

export default App;

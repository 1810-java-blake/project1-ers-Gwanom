import React, { Component } from 'react';
import './App.css';
import './Include/bootstrap';
import { AppNav } from './Components/Nav.component';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { SignInComponent } from './Components/Signin.component';
import { Home } from './Components/Home.component';
import { LogOut } from './Components/Logout.component';
import { LoggedOut } from './Components/Signout.success.component';
import { Success } from './Components/Success.component';
import { Admin } from './Components/Admin.component';

class App extends Component {
  componentDidMount(){
    document.title="Revature ERS";
  }

  render() {
    return (
      <BrowserRouter>
        <>
          <AppNav />
          <div id="main-content-container">
            <Switch>
              <Route path="/home" component={Home} />
              <Route path="/log-out" component={LogOut} />
              <Route path="/logged-out" component={LoggedOut} />
              <Route path="/success" component={Success} />
              <Route path="/admin" component={Admin} />
              <Route path="/login" component={SignInComponent} />
              <Route path="" component={SignInComponent} />
            </Switch>
          </div>
        </>
      </BrowserRouter>
    );
  }
}

export default App;
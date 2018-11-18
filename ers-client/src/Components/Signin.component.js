import React from 'react';

export class SignInComponent extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      username: '',
      password: '',
      failed: false
    }
  }

  passwordChange = (e) => {
    this.setState({
      ...this.state,
      password: e.target.value
    })
  }

  usernameChange = (e) => {
    this.setState({
      ...this.state,
      username: e.target.value
    })
  }

  submit = (e) => {
    e.preventDefault();
    let cred = {
      username: this.state.username,
      password: this.state.password
    }
    fetch('http://localhost:8080/ERS/users/login', {
      method: 'POST',
      body: JSON.stringify(cred),
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    })
      .then(res => {
        if (res.status === 403) {
          this.setState({
            ...this.state,
            failed: true
          })
          throw new Error(res);
        }
        return res;
      })
      .then(res => res.json())
      .then(res => {
        sessionStorage.setItem("role", res.role.toLowerCase());
        sessionStorage.setItem("name", res.name);
        sessionStorage.setItem("u_id", res.id);
      })
      .then(() => {
        if ("manager" === sessionStorage.getItem("role")) {
          this.props.history.push('/admin');

        } else if ("employee" === sessionStorage.getItem("role")) {
          this.props.history.push('/home');
        }
      })
      .catch(err => {
        console.log(err);
      })
  }

  render() {
    return (
      <form className="form-signin" onSubmit={this.submit}>
        <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>

        <label htmlFor="input-username" className="sr-only">Username</label>
        <input type="text"
          id="input-username"
          className="form-control"
          placeholder="Username"
          required
          value={this.state.username}
          onChange={this.usernameChange}
        />

        <label htmlFor="inputPassword" className="sr-only">Password</label>
        <input type="password"
          id="inputPassword"
          className="form-control"
          placeholder="Password"
          required
          value={this.state.password}
          onChange={this.passwordChange} />

        <button className="btn btn-primary btn-block"
          type="submit">
          Sign in
        </button>
        {
          (this.state.failed === true) &&
          <div class="alert alert-danger" role="alert">
            Invalid username and/or password
          </div>
        }
        <p className="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
      </form>
    )
  }
}
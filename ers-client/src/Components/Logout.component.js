import React from 'react';

export class LogOut extends React.Component {

    signOff(){
        fetch("htpp://localhost:8080/ERS/users/logout", {
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST'
        })
        .then(resp => {
            if(resp === 200){ this.props.history.push('/logged-out'); }
        })
        .catch(err => console.log(err))

    }

    render() {
        return(
        <button className="btn btn-sm btn-dark"
            onClick={this.signOff}>
            Sign Off
         </button>
        )
    }
}
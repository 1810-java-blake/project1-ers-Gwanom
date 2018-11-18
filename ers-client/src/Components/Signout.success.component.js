import React from 'react';

export class LoggedOut extends React.Component{
    
    componentDidMount(){
        fetch("htpp://localhost:8080/ERS/users/logout", {
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST'
        })
        .catch(err => console.log(err))
    }
    
    render(){
        return(
            <p>Successfully Signed Out</p>
        );
    }

}
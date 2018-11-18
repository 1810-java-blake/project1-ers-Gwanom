import React from 'react';

export class Success extends React.Component{
 componentDidMount(){
     window.setTimeout(() => {
         this.props.history.push("/home");
     }, 3000);
 }
 
    render(){
        return(
            <h3>Success!</h3>
        );
    }
}
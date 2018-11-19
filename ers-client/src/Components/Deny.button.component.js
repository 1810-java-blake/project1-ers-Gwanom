import React from 'react';

export class DenyButton extends React.Component {

    deny = () => {
        fetch(`http://localhost:8080/ERS/reimbs/${this.props.reimbId}/3`, {
            method: 'put',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
    }

    render() {
        return (
            <button type="button" className="btn btn-danger" onClick={this.deny}>
                Deny
            </button>
        );
    }
}
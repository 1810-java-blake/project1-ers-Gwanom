import React from 'react';

export class ApproveButton extends React.Component {

    approve = () => {
        fetch(`http://localhost:8080/ERS/reimbs/${this.props.reimbId}/2`, {
            method: 'put',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
    }

    render() {
        return (
            <button type="button" className="btn btn-success" onClick={this.approve}>
                Approve
            </button>
        );
    }
}
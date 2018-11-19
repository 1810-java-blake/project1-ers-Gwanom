import React from 'react';
import { FullReimbursement } from './Full.Reimbursement.component';

export class Admin extends React.Component {

    constructor() {
        super();
        this.state = {
            name: sessionStorage.getItem('name'),
            role: sessionStorage.getItem('role'),
            u_id: sessionStorage.getItem('u_id'),
            reimbursements: []
        };
    }

    componentDidMount() {
        fetch(`http://localhost:8080/ERS/reimbs/status/1`, {
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
            .then(resp => resp.json())
            .then(data => {
                console.log(data);
                this.setState({
                    ...this.state,
                    reimbursements: data
                })
            })
            .catch(err => console.log(err));
    }

    render() {
        let tableStyle = {
            width: "90%",
            margin: "auto"
        }
        return (
            <div>
                <h3 className="table-title">
                    Pending Reimbursement Requests
                </h3>
                <table className="table table-hover" style={tableStyle}>

                    <thead>
                        <tr>
                            <th scope="col">Amount</th>
                            <th scope="col">Time Submitted</th>
                            <th scope="col">Description</th>
                            <th scope="col">Submitted By</th>
                            <th scope="col">Type</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.reimbursements.map(reimbursement =>
                            <FullReimbursement
                                key={reimbursement.reimbursementId}
                                reimbursement={reimbursement} />
                        )}
                    </tbody>
                </table>
            </div>
        );
    }
}
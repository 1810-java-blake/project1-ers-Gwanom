import React from 'react';
import { Reimbursement } from './Reimbursement.component';

export class Home extends React.Component {
    constructor() {
        super();
        this.state = {
            name: sessionStorage.getItem('name'),
            role: sessionStorage.getItem('role'),
            u_id: sessionStorage.getItem('u_id'),
            newAmount: null,
            newType: null,
            newDesc: null,
            reimbursements: []
        };
    }

    componentDidMount() {
        fetch(`http://localhost:8080/ERS/users/${this.state.u_id}/reimb`, {
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
            .then(resp => resp.json())
            .then(data => {
                this.setState({
                    ...this.state,
                    reimbursements: data
                })
            })
            .catch(err => console.log(err));
    }

    amountChange = (e) => {
        this.setState({
            ...this.state,
            newAmount: e.target.value
        });
    }

    typeChange = (e) => {
        this.setState({
            ...this.state,
            newType: e.target.value
        });
    }

    descChange = (e) => {
        this.setState({
            ...this.state,
            newDesc: e.target.value
        });
    }

    submitNew = () => {
        //      /users/1/reimb
        // document.getElementById('formModal').modal('hide');
        let request = {
            reimbursementId: null,
            reimbursementAmount: this.state.newAmount,
            submitted: null,
            resolved: null,
            description: this.state.newDesc,
            authorUserId: this.state.u_id,
            resolverUserId: null,
            statusId: 1,
            reimbursementTypeId: this.state.newType
        };

        fetch(`http://localhost:8080/ERS/users/${request.authorUserId}/reimb`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request),
            credentials: "include"
        })
        .then(res => { 
            if(!(res.status ===0)){
                this.props.history.push("/success");
            }
        })
    }

    render() {
        return (
            <>
                <div>
                    <h3 className="table-title">
                        {this.state.name}'s Reimbursement Requests
                </h3>
                    <table className="table table-hover">
                        <thead>
                            <tr>
                                <th scope="col">Amount</th>
                                <th scope="col">Time Submitted</th>
                                <th scope="col">Description</th>
                                <th scope="col">Status</th>
                                <th scope="col">Type</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.reimbursements.map(reimbursement =>
                                <Reimbursement
                                    key={reimbursement.reimbursementId}
                                    reimbursement={reimbursement} />
                            )}
                        </tbody>
                    </table>
                </div>
                <div>
                    <button button type="button" className="btn btn-primary" data-toggle="modal" data-target="#formModal">
                        Submit a New Reimbursement Request
                    </button>

                    <div className="modal fade" id="formModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel" aria-hidden="true">
                        <div className="modal-dialog" role="document">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title" id="formModalLabel">New Reimbursement</h5>
                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div className="modal-body">
                                    <form>
                                        <div className="form-group">
                                            <label htmlFor="reimbursement-amount" className="col-form-label">Amount:</label>
                                            <input type="text" className="form-control" id="reimbursement-amount" onChange={this.amountChange} />
                                        </div>
                                        <fieldset className="form-group">
                                            <div className="row">
                                                <legend className="col-form-label col-sm-2 pt-0">Type of Expense:</legend>
                                                <div className="col-sm-10">
                                                    <div className="form-check">
                                                        <input className="form-check-input" type="radio" name="gridRadios" id="gridRadios1" value="2" onClick={this.typeChange} />
                                                        <label className="form-check-label" htmlFor="gridRadios1">
                                                            Travel
                                                        </label>
                                                    </div>
                                                    <div className="form-check">
                                                        <input className="form-check-input" type="radio" name="gridRadios" id="gridRadios2" value="3" onClick={this.typeChange} />
                                                        <label className="form-check-label" htmlFor="gridRadios2">
                                                            Food
                                                        </label>
                                                    </div>
                                                    <div className="form-check">
                                                        <input className="form-check-input" type="radio" name="gridRadios" id="gridRadios3" value="1" onClick={this.typeChange} />
                                                        <label className="form-check-label" htmlFor="gridRadios3" >
                                                            Lodging
                                                        </label>
                                                    </div>
                                                    <div className="form-check">
                                                        <input className="form-check-input" type="radio" name="gridRadios" id="gridRadios4" value="4" onClick={this.typeChange} />
                                                        <label className="form-check-label" htmlFor="gridRadios4" >
                                                            Other
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </fieldset>
                                        <div className="form-group">
                                            <label htmlFor="description-text" className="col-form-label">Description:</label>
                                            <textarea className="form-control" id="description-text" onChange={this.descChange}></textarea>
                                        </div>
                                    </form>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="button" className="btn btn-primary" data-toggle="modal" data-target="#formModal"
                                    onClick={this.submitNew}>
                                        Submit
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </>
        );
    }
}
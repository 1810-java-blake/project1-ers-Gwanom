import React, { PureComponent } from 'react';
import { MdFlight, MdHotel, MdRestaurant, MdShoppingBasket} from 'react-icons/md';

export class FullReimbursement extends PureComponent {
    state = {
        color: null,
        status: null,
    }

    timeConverter(UNIX_timestamp) {
        var a = new Date(UNIX_timestamp);
        var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var date = a.getDate();
        var hour = a.getHours();
        var min = a.getMinutes();
        var sec = a.getSeconds();
        var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
        return time;
    }

    componentDidMount() {
        switch (this.props.reimbursement.statusId) {
            case 1:
                this.setState({
                    color: 'table-warning',
                    status: "PENDING"
                });
                break;
            case 2:
                this.setState({
                    color: "table-success",
                    status: "APPROVED"
                });
                break;
            case 3:
                this.setState({
                    color: 'table-danger',
                    status: "DENIED"
                });
                break;
            default:
                return "";
        }
    }

    render() {
        const { reimbursementAmount, submitted, description, authorUserId } = this.props.reimbursement;

        return (
            <tr>
                <td>${reimbursementAmount}</td>
                <td>{this.timeConverter(submitted)}</td>
                <td>{description}</td>
                <td>{authorUserId}</td>
                <td>
                    {(this.props.reimbursement.reimbursementTypeId === 1) && <div title="lodging"><MdHotel className="type-icon"/></div>}
                    {(this.props.reimbursement.reimbursementTypeId === 2) && <div title="travel"><MdFlight className="type-icon"/></div>}
                    {(this.props.reimbursement.reimbursementTypeId === 3) && <div title="food"><MdRestaurant className="type-icon"/></div>}
                    {(this.props.reimbursement.reimbursementTypeId === 4) && <div title="other"><MdShoppingBasket className="type-icon"/></div>}
                </td>
            </tr>
        );
    }


}
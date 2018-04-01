import React from "react";
import {string, number, arrayOf, shape} from "prop-types";

const JoinPanel = ({roomsInfo}) => {
    return (
        <fieldset className='fieldset'>
            <legend className='legend'>Join a room</legend>
            {roomsInfo.length
                ? <table className='join-room-table'>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Type</th>
                                <th>Infos</th>
                                <th>Players</th>
                                <th/>
                            </tr>
                        </thead>
                        <tbody>
                            {roomsInfo.map(room => <tr key={room}>
                                <td>{room.title}</td>
                                <td>{room.type}</td>
                                <td>{room.packsInfo}</td>
                                <td>{room.usedSeats}/{room.totalSeats}</td>
                                <td>
                                    <a href={`/${room.id}`} className='join-room-link'>
                                        Join room
                                    </a>
                                </td>
                            </tr>)}
                        </tbody>
                    </table>
                : "There are no rooms currently open."}
        </fieldset>
    );
};

JoinPanel.propTypes = {
    roomsInfo: arrayOf(shape({title: string, type: string, packsInfo: string, usedSeats: number.isRequired, totalSeats: number.isRequired}).isRequired).isRequired
};

export default JoinPanel;

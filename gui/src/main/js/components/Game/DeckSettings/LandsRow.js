import React from "react";
import {string, array, object, func} from "prop-types";
import {CARDS} from "../../../reducers/playerState";

const LandsRow = ({zoneName, cards, onChangeLand}) => (
    <tr>
        <td>{zoneName}</td>
        {Object.keys(CARDS).map((cardName, index) =>
            <td key={index}>
                <input
                    className='number'
                    min={0}
                    onChange={(event) => onChangeLand({zoneName, cardName, event})}
                    type='number'
                    value={cards.filter(({name}) => name === cardName).length || 0}/>
            </td>)}
    </tr>
);

LandsRow.propTypes = {
    zoneName: string.isRequired,
    cards: array.isRequired,
    onChangeLand: func.isRequired
};

export default LandsRow;

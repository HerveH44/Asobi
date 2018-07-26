import React from "react"
import {array, func, number, object} from "prop-types";
import LandsRow from "./LandsRow";
import {connect} from "react-redux";
import {onChangeDeckSize, onChangeLand, onSuggestLands, onResetLands} from "../../actions/game";
import uniqueId from "lodash/uniqueId";
import {MAIN, SIDE} from "../../reducers/playerState";
import game from "../../reducers/game";

const LandsPanel = ({cards, onChangeLand, onResetLands, onSuggestLands, onChangeDeckSize, deckSize}) => (
    <fieldset className='land-controls fieldset'>
        <legend className='legend game-legend'>Lands</legend>
        <table>
            <thead>
            <ManaSymbols/>
            </thead>
            <tbody>
            <LandsRow zoneName={MAIN} onChangeLand={onChangeLand} cards={cards[MAIN]}/>
            <LandsRow zoneName={SIDE} onChangeLand={onChangeLand} cards={cards[SIDE]}/>
            </tbody>
            <tfoot>
            <SuggestLands onResetLands={onResetLands} onSuggestLands={onSuggestLands} onChangeDeckSize={onChangeDeckSize} deckSize={deckSize}/>
            </tfoot>
        </table>
    </fieldset>
);

LandsPanel.propTypes = {
    cards: object.isRequired,
    onChangeLand: func.isRequired,
    onResetLands: func.isRequired,
    onSuggestLands: func.isRequired,
    onChangeDeckSize: func.isRequired,
    deckSize: number.isRequired
};


const ManaSymbols = () => {
    const manaSymbols = ["White", "Blue", "Black", "Red", "Green"];
    const url = x => `https://www.wizards.com/Magic/redesign/${x}_Mana.png`;

    return (
        <tr>
            <td/>
            {manaSymbols.map(x =>
                <td key={uniqueId()}>
                    <img src={url(x)} alt={x}/>
                </td>)
            }
        </tr>
    );
};

const SuggestLands = ({onResetLands, onSuggestLands, onChangeDeckSize, deckSize}) => (
    <tr>
        <td>deck size</td>
        <td>
            <input
                className='number'
                min={0}
                onChange={onChangeDeckSize}
                type='number'
                value={deckSize}/>
        </td>
        <td colSpan={2}>
            <button className='land-suggest-button' onClick={onResetLands}>
                reset lands
            </button>
        </td>
        <td colSpan={2}>
            <button className='land-suggest-button' onClick={onSuggestLands}>
                suggest lands
            </button>
        </td>
    </tr>
);

SuggestLands.propTypes = {
    onResetLands: func.isRequired,
    onSuggestLands: func.isRequired,
    onChangeDeckSize: func.isRequired,
    deckSize: number.isRequired
};

const mapStateToProps = ({gameState, playerState: {Main, Side}}) => ({
    cards: { Main, Side },
    deckSize: gameState.deckSize
});

const mapDispatchToProps = {
    onChangeLand, onChangeDeckSize, onSuggestLands, onResetLands
};

export default connect(mapStateToProps, mapDispatchToProps)(LandsPanel);

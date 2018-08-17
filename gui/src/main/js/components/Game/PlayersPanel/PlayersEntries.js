import React, {Component} from "react";
import {array, object, number, bool, func, string} from "prop-types";
import {onSwap, onKick, onChangePlayerName} from "../../../actions/game"
import {onSetName, onSetPlayerName} from "../../../actions/server"
import arrowUp from "../../../../resources/media/arrow-up.png"
import arrowDown from "../../../../resources/media/arrow-down.png"

import {connect} from "react-redux"

class PlayerEntries extends Component {
    render() {
        return (
            this.props.playersStates
                .sort((a, b) => a.seat - b.seat)
                .map((p, i) =>
                    <PlayerEntry
                        key={i}
                        player={p}
                        index={i}
                        {...this.props} />)
        );
    }
}

PlayerEntries.propTypes = {
    playersStates: array.isRequired,
    self: number.isRequired,
    didGameStart: bool.isRequired,
    isHost: bool.isRequired,
    onSwap: func.isRequired,
    onKick: func.isRequired
};

const PlayerEntry = ({player: {isBot, name, packs, time, cockHash, mwsHash}, myName, onChangePlayerName, onSetPlayerName, index, playersStates: {length}, self, didGameStart, isHost, onSwap, onKick}) => {
    const opp
        = length % 2 === 0
        ? (self + length / 2) % length
        : null;

    const className
        = index === self
        ? "self"
        : index === opp
            ? "opp"
            : null;

    const connectionStatusIndicator
        = <span className={isBot ? "icon-bot" : "icon-connected"}
                title={isBot ? "This player is a bot." : ""}/>;

    const columns = [
        <td key={0}>{index + 1}</td>,
        <td key={1}>{connectionStatusIndicator}</td>,
        <td key={2}>{index === self
            ? <input type='text'
                     size={10}
                     maxLength={10}
                     value={myName}
                     onBlur={onSetPlayerName}
                     onChange={onChangePlayerName}/>
            : name}</td>,
        <td key={3}>{packs}</td>,
        <td key={4}>{time}</td>,
        <td key={5}>{cockHash}</td>,
        <td key={6}>{mwsHash}</td>
    ];

    if (isHost && !didGameStart) {
        //Move Player Button
        columns.push(
            <td key={7}>
                <button onClick={() => onSwap([index, index + 1])}>
                    <img src={arrowUp} width="16px"/>
                </button>
                <button onClick={() => onSwap([index, index - 1])}>
                    <img src={arrowDown} width="16px"/>
                </button>
            </td>);
        //Kick Player button
        columns.push(
            index !== self && !isBot ?
                <td key={8}>
                    <button onClick={() => onKick(index)}>
                        kick
                    </button>
                </td>
                : <td key={9}/>);
    }

    return <tr className={className}>{columns}</tr>;
};

PlayerEntry.propTypes = {
    player: object.isRequired,
    index: number.isRequired,
    playersStates: array.isRequired,
    self: number.isRequired,
    didGameStart: bool.isRequired,
    isHost: bool.isRequired,
    onSwap: func.isRequired,
    onKick: func.isRequired,
    onChangePlayerName: func.isRequired,
    onSetPlayerName: func.isRequired,
    myName: string.isRequired
};

const mapStateToProps = ({gameState, player}) => ({
    ...gameState,
    myName: player.name
});

const mapDispatchToProps = {
    onSwap,
    onKick,
    onChangePlayerName,
    onSetPlayerName
};

export default connect(mapStateToProps, mapDispatchToProps)(PlayerEntries)

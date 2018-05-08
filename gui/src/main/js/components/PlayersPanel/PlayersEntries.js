import React, {Component} from "react";
import {array, object, number, bool, func} from "prop-types";
import uniqueId from "lodash/uniqueId"
import {onSwap, onKick} from "../../actions/game"
import arrowUp from "../../../resources/media/arrow-up.png"
import arrowDown from "../../../resources/media/arrow-down.png"

import { connect } from "react-redux"

class PlayerEntries extends Component {
    decrement() {
        //TODO: add a reducer method
        console.log("add reducer for decrement method")
        // for (let p of this.props.players)
        //     if (p.time)
        //         p.time--;
    }
    componentDidMount() {
         this.timer = window.setInterval(this.decrement.bind(this), 1e3);
    }
    componentWillUnmount() {
        window.clearInterval(this.timer);
    }
    render() {
        return (
            this.props.players.map((p,i) =>
                <PlayerEntry
                    key ={uniqueId()}
                    player={p}
                    index={i}
                    {...this.props} />)
        );
    }
}

PlayerEntries.propTypes = {
    players: array.isRequired,
    self: number.isRequired,
    didGameStart: bool.isRequired,
    isHost: bool.isRequired,
    onSwap: func.isRequired,
    onKick: func.isRequired
}

const PlayerEntry = ({player: {isBot, name, packs, time, hash}, index, players: {length}, self, didGameStart, isHost, onSwap, onKick}) => {
    const opp
    = length % 2 === 0
        ? (self + length/2) % length
        : null;

    const className
    = index === self
        ? "self"
        : index === opp
        ? "opp"
        : null;

    const connectionStatusIndicator
    = <span className={isBot ? "icon-bot" : "icon-connected"}
        title={isBot ? "This player is a bot.": ""} />;

    const columns = [
        <td key={uniqueId()}>{index + 1}</td>,
        <td key={uniqueId()}>{connectionStatusIndicator}</td>,
        <td key={uniqueId()}>{name}</td>,
        <td key={uniqueId()}>{packs}</td>,
        <td key={uniqueId()}>{time}</td>,
        <td key={uniqueId()}>{hash && hash.cock}</td>,
        <td key={uniqueId()}>{hash && hash.mws}</td>
    ];

    if (isHost) {
        //Move Player
        if(!didGameStart)
            columns.push(
                <td key={uniqueId()}>
                    <button onClick={()=> onSwap([index, index - 1])}>
                        <img src={arrowDown} width="16px"/>
                    </button>
                    <button onClick={()=> onSwap([index, index + 1])}>
                        <img src={arrowUp} width="16px"/>
                    </button>
                </td>);
        //Kick button
        if (index !== self && !isBot) {
            columns.push(
                <td key={uniqueId()}>
                    <button onClick={()=> onKick(index)}>
                        kick
                    </button>
                </td>);
        } else {
            columns.push(<td key={uniqueId()}/>);
        }
    }

    return <tr key={uniqueId()} className={className}>{columns}</tr>;
};

PlayerEntry.propTypes = {
    player: object.isRequired,
    index: number.isRequired,
    players: array.isRequired,
    self: number.isRequired,
    didGameStart: bool.isRequired,
    isHost: bool.isRequired,
    onSwap: func.isRequired,
    onKick: func.isRequired
};

const mapStateToProps = ({gameState}) => ({
    ...gameState
})

const mapDispatchToProps = {
    onSwap,
    onKick
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerEntries)
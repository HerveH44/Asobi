import React from "react"
import {connect} from 'react-redux';
import {string, array, number, object, func} from "prop-types"
import {Spaced} from "../utils";
import {pick, autoPick} from "../../actions/server"
import MainZone from "./MainZone";
import Grid from "./Grid";
import PackZone from "./PackZone";
import {getCardsAsArray, PACK} from "../../reducers/playerState";

const CardsZone = ({cards, round, pick, autoPick, autoPickId}) => (
    <div>
        <PackZone cards={cards}
                  round={round}
                  pick={pick}
                  autoPick={autoPick}
                  autoPickId={autoPickId}/>
        <MainZone/>
    </div>
);

CardsZone.propTypes = {
    round: number.isRequired,
    waitingPack: object,
    autoPick: func.isRequired,
    pick: func.isRequired,
    autoPickId: string.isRequired
};

const mapStateToProps = ({playerState, gameState, gameSettings}) => ({
    ...playerState,
    ...gameState,
    cards: getCardsAsArray(playerState, PACK, gameSettings.sort)
});
const mapDispatchToProps = {
    pick, autoPick
};

export default connect(mapStateToProps, mapDispatchToProps)(CardsZone);

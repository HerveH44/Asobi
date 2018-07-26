import React from "react"
import {connect} from 'react-redux';
import {string, array, number, object, func} from "prop-types"
import {Spaced} from "../utils";
import Card from "./Card";
import uniqueId from "lodash.uniqueid"
import {pick, autoPick} from "../../actions/server"
import {MAIN, SIDE} from "../../reducers/playerState";
import MainZone from "./MainZone";
import Grid from "./Grid";

const CardsZone = ({waitingPack, round, pick, autoPick, autoPickId}) => (
    <div>
        <PackZone cards={(waitingPack || {}).cards || []} round={round}
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

const PackZone = ({cards, round, pick, autoPick, autoPickId}) => (
    <Grid
        zoneName={"Pack"}
        zoneTitle={`Pack ${round}`}
        // TODO: add pick in state
        zoneSubtitle={`Pick TODO`}
        cards={cards}
        pick={pick}
        autoPick={autoPick}
        autoPickId={autoPickId}/>
);

PackZone.propTypes = {
    cards: array.isRequired,
    round: number.isRequired,
    pick: func.isRequired,
    autoPick: func.isRequired,
    autoPickId: string.isRequired
};

const mapStateToProps = ({playerState, gameState}) => ({
    ...playerState,
    ...gameState
});
const mapDispatchToProps = {
    pick, autoPick
};

export default connect(mapStateToProps, mapDispatchToProps)(CardsZone);

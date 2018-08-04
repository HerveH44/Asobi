import React  from "react"
import {string, array, number, object, func} from "prop-types"
import Grid from "./Grid";


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

export default PackZone;

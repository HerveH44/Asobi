import React from "react"
import {string, array, number, object, func} from "prop-types"
import Grid from "./Grid";
import {getCardsAsArray, PACK} from "../../../reducers/playerState";
import {connect} from "react-redux";
import {autoPick, pick} from "../../../actions/server";

const PackZone = ({cards, pickNumber, round, autoPickId, pick, autoPick, addCardClassNames, addCardTitle}) => (
    !cards.length ?
        <div/> :
        <Grid
            zoneName={"Pack"}
            zoneTitle={`Pack ${round}`}
            zoneSubtitle={`Pick ${pickNumber}`}
            cards={cards}
            addCardClassNames={addCardClassNames}
            addCardTitle={addCardTitle}
            onClickCard={({id}) => () => {
                id === autoPickId ? pick(id) : autoPick(id);
            }}/>
);

PackZone.propTypes = {
    cards: array.isRequired,
    round: number.isRequired,
    pickNumber: number.isRequired,
    autoPickId: string.isRequired,
    pick: func.isRequired,
    autoPick: func.isRequired,
    addCardClassNames: func.isRequired,
    addCardTitle: func.isRequired,
};

const mapStateToProps = ({gameState, playerState, gameSettings}) => ({
    round: gameState.round,
    pickNumber: playerState.pickNumber,
    cards: getCardsAsArray(playerState, PACK, gameSettings.sort),
    autoPickId: playerState.autoPickId,
    addCardTitle: ({id}) => (
        id === playerState.autoPickId ? "This card will be automatically picked if your time expires." : ""
    ),
    addCardClassNames: ({id}) => (
        id === playerState.autoPickId ? "card autopick-card" : "card"
    )
});
const mapDispatchToProps = {
    pick, autoPick
};

export default connect(mapStateToProps, mapDispatchToProps)(PackZone);

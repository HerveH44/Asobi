import React from "react";
import {array, func} from "prop-types";
import Grid from "./Grid";
import {connect} from "react-redux";
import {getCardsAsArray, MAIN} from "../../../reducers/playerState";
import {clickCardZone} from "../../../actions/game";

const MainZone = ({cards, onClickZone}) => (
    <Grid
        zoneName={"Main"}
        zoneTitle={"Main"}
        zoneSubtitle={"" + cards.length}
        cards={cards}
        addCardClassNames={() => "card"}
        onClickCard={(card) => () =>  onClickZone({zone: MAIN, card})}/>
);

MainZone.propTypes = {
    cards: array.isRequired,
    onClickZone: func.isRequired
};

const mapStateToProps = ({playerState, gameSettings}) => ({
    cards: getCardsAsArray(playerState, MAIN, gameSettings.sort)
});
const mapDispatchToProps = {
    onClickZone: clickCardZone
};

export default connect(mapStateToProps, mapDispatchToProps)(MainZone);

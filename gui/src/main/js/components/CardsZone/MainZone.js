import React from "react";
import {array} from "prop-types";
import Grid from "./Grid";
import {connect} from "react-redux";
import {autoPick, pick} from "../../actions/server";
import {getCardsAsArray, MAIN} from "../../reducers/playerState";

const MainZone = ({cards}) => (
    <Grid
        zoneName={"Main"}
        zoneTitle={"Main"}
        zoneSubtitle={"" + cards.length}
        autoPick={() => ""}
        autoPickId={""}
        pick={() => ""}
        cards={cards}/>
);

MainZone.propTypes = {
    cards: array.isRequired
};

const mapStateToProps = ({playerState, gameSettings}) => ({
    cards: getCardsAsArray(playerState, MAIN, gameSettings.sort)
});
const mapDispatchToProps = {
    pick, autoPick
};

export default connect(mapStateToProps, mapDispatchToProps)(MainZone);

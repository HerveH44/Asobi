import React from "react";
import {array, object} from "prop-types";
import Grid from "./Grid";
import {connect} from "react-redux";
import {autoPick, pick} from "../../actions/server";
import {MAIN} from "../../reducers/playerState";

const MainZone = ({Main: cards}) => (
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
    Main: array.isRequired
};

const mapStateToProps = ({playerState}) => ({
    ...playerState,
});
const mapDispatchToProps = {
    pick, autoPick
};

export default connect(mapStateToProps, mapDispatchToProps)(MainZone);

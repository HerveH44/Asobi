import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";

import _ from "lodash";

const NewsPanel = ({motd}) => (
    <fieldset className='fieldset'>
        <legend className='legend'>News</legend>
        {motd.map(x => <li key={_.uniqueId()}>{x}</li>)}
    </fieldset>
);

NewsPanel.propTypes = {
    motd: PropTypes.array
};

const mapStateToProps = (state) => ({
  motd: state.def.motd
});

export default connect(mapStateToProps)(NewsPanel);

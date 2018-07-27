import React from "react";
import PropTypes from "prop-types";

const NewsPanel = ({motd}) => (
    <fieldset className='fieldset'>
        <legend className='legend'>News</legend>
        {motd.map((x, key) => <li key={key}>{x}</li>)}
    </fieldset>
);

NewsPanel.propTypes = {
    motd: PropTypes.array
};

export default NewsPanel;

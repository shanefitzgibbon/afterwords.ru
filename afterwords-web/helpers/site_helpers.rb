# encoding: UTF-8

module SiteHelpers

  def page_title
    title = "AfterWords"
    if data.page.title
      title << " | " + data.page.title
    end
    title
  end
  
  def page_description
    if data.page.description
      description = data.page.description
    else
      description = "AfterWords | Editing and coaching for your English writing"
    end
    description
  end

end
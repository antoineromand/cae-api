{{- range .Versions }}
<a name="{{ .Tag.Name }}"></a>
## 📦 Version {{ .Tag.Name }} {{ if .Tag.Previous }}([diff]({{ $.Info.RepositoryURL }}/compare/{{ .Tag.Previous.Name }}...{{ .Tag.Name }})){{ end }}

🗓️  _{{ datetime "2006-01-02" .Tag.Date }}_

{{ range .CommitGroups }}
### {{ if eq .Title "Feat" }}✨ Features
{{ else if eq .Title "Fix" }}🐛 Fixes
{{ else if eq .Title "Chore" }}🧹 Chores
{{ else }}📌 {{ .Title }}
{{ end }}

{{- range .Commits }}
- {{ if .Scope }}**{{ .Scope }}**: {{ end }}{{ .Subject }}
  {{- end }}

{{ end }}

{{- if .NoteGroups }}
{{ range .NoteGroups }}
### ⚠️ {{ .Title }}
{{ range .Notes }}
- {{ .Body }}
  {{ end }}
  {{ end }}
  {{ end }}

---

{{ end }}
